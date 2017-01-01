package com.babar.core.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.babar.common.BabarService;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

@PropertySource({"classpath:config.properties", "classpath:zookeeper.config"})
@Component
public class BabarServer implements DisposableBean{

	@Value("${babar.server.host}")
	private String babarServerHost;

	@Value("${babar.server.port}")
	private int babarServerPort;

	@Value("${babar.server.netty.boss.thread.number}")
	private int babarServerNettyBossThreadNumber;

	@Value("${babar.server.netty.worker.thread.number}")
	private int babarServerNettyWorkerThreadNumber;

	@Value("${babar.server.worker.thread.number}")
	private int babarServerWorkerThreadNumber;

	@Value("${zookeeper.registry.path}" + "${zookeeper.data.path}")
	private String zooKeeperBabarServerPath;

	@Autowired
	private BabarServiceRegistry babarServiceRegistry;

	private Map<String, Object> handlerMap = new HashMap<String, Object>();
	Log log = LogFactory.getLog(this.getClass());


	@EventListener
	public void handleContextRefresh(ContextRefreshedEvent event){
		registerService(event);
		EventLoopGroup bossGroup = new NioEventLoopGroup(babarServerNettyBossThreadNumber);
		EventLoopGroup workerGroup = new NioEventLoopGroup(babarServerNettyWorkerThreadNumber);
		EventExecutorGroup executorGroup = new DefaultEventExecutorGroup(babarServerWorkerThreadNumber, new BabarServerWorkerThreadFactory());
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(new BabarServerInitializer(handlerMap, executorGroup));
			babarServiceRegistry.initRegistryPath(); /*remove this if want to register multiple services under same parent path*/
			babarServiceRegistry.registerService(zooKeeperBabarServerPath, babarServerHost + ":" + babarServerPort);
			log.info("Babar Server started on: " + babarServerHost + ":" + babarServerPort);
			b.bind(babarServerHost, babarServerPort).sync().channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	private void registerService(ContextRefreshedEvent event){
		ApplicationContext ctx = event.getApplicationContext();
		Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(BabarService.class);
		for(Object bean : serviceBeanMap.values()){
			String interfaceName = bean.getClass().getAnnotation(BabarService.class).value().getName();
			handlerMap.put(interfaceName, bean);
			log.info("Register service " + interfaceName + " with bean " + bean.getClass().getName());
		}
	}
	@Override
	public void destroy() throws Exception {
		babarServiceRegistry.destroyRegistryPath(); /*remove this if want to register multiple services under same parent path*/
		babarServiceRegistry.close();
	}
}
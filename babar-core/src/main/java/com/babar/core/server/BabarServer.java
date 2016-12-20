package com.babar.core.server;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.babar.common.BabarService;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@PropertySource("classpath:config.properties")
@Component
public class BabarServer {
	@Autowired
	private Environment env;
	private Map<String, Object> handlerMap = new HashMap<String, Object>();

	@EventListener
	public void handleContextRefresh(ContextRefreshedEvent event) throws UnknownHostException {
		registerService(event);
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(new BabarServerInitializer(handlerMap));
			b.bind(env.getProperty("server.host"), Integer.parseInt(env.getProperty("server.port"))).sync().channel().closeFuture().sync();
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
		}
	}
}

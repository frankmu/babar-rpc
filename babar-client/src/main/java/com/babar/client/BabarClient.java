package com.babar.client;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.babar.common.BabarRequest;
import com.babar.sample.service.IBabarSampleService;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

@PropertySource("classpath:config.properties")
@Component
public class BabarClient {
	@Autowired
	private Environment env;

	@EventListener
	public void handleContextRefresh(ContextRefreshedEvent event){
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group);
			b.channel(NioSocketChannel.class);
			b.handler(new BabarClientInitializer());
			Channel ch = b.connect(env.getProperty("server.host"), Integer.parseInt(env.getProperty("server.port"))).sync().channel();
			BabarRequest req = new BabarRequest();
			req.setRequestId(UUID.randomUUID().toString());
			req.setClassName(IBabarSampleService.class.getName());
			req.setMethodName("Hello");
			String[] parameters = {"World"};
			req.setParameters(parameters);
			Class<?>[] parameterTypes = {String.class};
			req.setParameterTypes(parameterTypes);
			ch.writeAndFlush(req);
			ch.closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
}
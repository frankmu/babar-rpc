package com.babar.client;

import java.util.UUID;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.babar.common.BabarRequest;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

@Component
public class BabarClient {
	@EventListener
	public void handleContextRefresh(ContextRefreshedEvent event){
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group);
			b.channel(NioSocketChannel.class);
			b.handler(new BabarClientInitializer());
			Channel ch = b.connect("127.0.1.1", 7000).sync().channel();
			BabarRequest req = new BabarRequest();
			req.setRequestId(UUID.randomUUID().toString());
			ch.writeAndFlush(req);
			ch.closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
}
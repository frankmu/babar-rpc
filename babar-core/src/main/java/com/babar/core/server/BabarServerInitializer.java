package com.babar.core.server;

import java.util.Map;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.EventExecutorGroup;

public class BabarServerInitializer extends ChannelInitializer<SocketChannel>{
	
	private Map<String, Object> handlerMap;
	private EventExecutorGroup executorGroup;

	public BabarServerInitializer(Map<String, Object> handlerMap, EventExecutorGroup executorGroup) {
		this.handlerMap = handlerMap;
		this.executorGroup = executorGroup;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
		pipeline.addLast(new LengthFieldPrepender(4));
		pipeline.addLast(new ObjectEncoder());
		pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
		pipeline.addLast(executorGroup, new BabarServerHandler(handlerMap));
	}
}
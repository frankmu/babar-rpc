package com.babar.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

@Component
public class BabarClientChannelPoolHandler implements ChannelPoolHandler{

	@Autowired
	BabarResponseMap babarResponseMap;

	@Override
	public void channelReleased(Channel ch) throws Exception {	
	}

	@Override
	public void channelAcquired(Channel ch) throws Exception {
	}

	@Override
	public void channelCreated(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
		pipeline.addLast(new LengthFieldPrepender(4));
		pipeline.addLast(new ObjectEncoder());
		pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));	
		pipeline.addLast(new BabarClientHandler(babarResponseMap));
	}

}

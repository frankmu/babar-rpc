package com.babar.client;

import org.springframework.stereotype.Component;

import com.babar.common.BabarResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Component
public class BabarClientHandler extends SimpleChannelInboundHandler<BabarResponse> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, BabarResponse msg) throws Exception {
		System.out.println("Received data from server: " + msg.getResult());
	}
}

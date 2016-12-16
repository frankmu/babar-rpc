package com.babar.core.server;

import com.babar.common.BabarRequest;
import com.babar.common.BabarResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class BabarServerHandler extends SimpleChannelInboundHandler<BabarRequest> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, BabarRequest req) throws Exception {
		System.out.println("Received Data from Client:" + req.getRequestId());
		BabarResponse response = new BabarResponse();
		response.setRequestId(req.getRequestId());
		ctx.writeAndFlush(response);
	}
}

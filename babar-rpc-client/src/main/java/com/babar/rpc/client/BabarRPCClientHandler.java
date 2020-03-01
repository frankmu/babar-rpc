package com.babar.rpc.client;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.babar.rpc.common.BabarRPCResponse;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class BabarRPCClientHandler extends ChannelDuplexHandler {

	private BabarRPCResponseMap babarRPCResponseMap;
	private static final Logger logger = LoggerFactory.getLogger(BabarRPCClientHandler.class);

	public BabarRPCClientHandler(BabarRPCResponseMap babarRPCResponseMap) {
		this.babarRPCResponseMap = babarRPCResponseMap;
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("Get response from server with message type " + msg.getClass().getName());
		if(msg instanceof BabarRPCResponse){
			BabarRPCResponse res = (BabarRPCResponse) msg;
			CompletableFuture<Object> future = babarRPCResponseMap.getResponseMap().get(res.getRequestId());
			future.complete(res.getResult());
		}
	}
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception{
		logger.info("Send request to server with message type: " + msg.getClass().getName());
		super.write(ctx, msg, promise);
	}
}

package com.babar.rpc.client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.babar.rpc.common.BabarRPCRequest;
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
			BlockingQueue<BabarRPCResponse> queue = babarRPCResponseMap.getResponseMap().get(res.getRequestId());
			queue.offer(res);
		}
	}
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception{
		logger.info("Send request to server with message type: " + msg.getClass().getName());
		if(msg instanceof BabarRPCRequest){
			BabarRPCRequest req = (BabarRPCRequest) msg;
			babarRPCResponseMap.getResponseMap().putIfAbsent(req.getRequestId(), new LinkedBlockingQueue<BabarRPCResponse>(1));
			logger.info("Create 1 record in response map with requestId: " + req.getRequestId());
		}
		super.write(ctx, msg, promise);
	}
}

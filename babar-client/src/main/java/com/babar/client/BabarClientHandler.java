package com.babar.client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.babar.common.BabarRequest;
import com.babar.common.BabarResponse;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class BabarClientHandler extends ChannelDuplexHandler {

	private BabarResponseMap babarResponseMap;
	private static final Logger logger = LoggerFactory.getLogger(BabarClientHandler.class);

	public BabarClientHandler(BabarResponseMap babarResponseMap) {
		this.babarResponseMap = babarResponseMap;
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("Get response from server with message type " + msg.getClass().getName());
		if(msg instanceof BabarResponse){
			BabarResponse res = (BabarResponse) msg;
			BlockingQueue<BabarResponse> queue = babarResponseMap.getResponseMap().get(res.getRequestId());
			queue.offer(res);
		}
	}
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception{
		logger.info("Send request to server with message type: " + msg.getClass().getName());
		if(msg instanceof BabarRequest){
			BabarRequest req = (BabarRequest) msg;
			babarResponseMap.getResponseMap().putIfAbsent(req.getRequestId(), new LinkedBlockingQueue<BabarResponse>(1));
			logger.info("Create 1 record in response map with requestId: " + req.getRequestId());
		}
		super.write(ctx, msg, promise);
	}
}

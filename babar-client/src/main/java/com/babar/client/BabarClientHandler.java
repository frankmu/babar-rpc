package com.babar.client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.babar.common.BabarRequest;
import com.babar.common.BabarResponse;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class BabarClientHandler extends ChannelDuplexHandler {

	private BabarResponseMap babarResponseMap;

	public BabarClientHandler(BabarResponseMap babarResponseMap) {
		this.babarResponseMap = babarResponseMap;
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof BabarResponse){
			BabarResponse res = (BabarResponse) msg;
			BlockingQueue<BabarResponse> queue = babarResponseMap.getResponseMap().get(res.getRequestId());
			queue.offer(res);
		}
	}
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception{
		if(msg instanceof BabarRequest){
			BabarRequest req = (BabarRequest) msg;
			babarResponseMap.getResponseMap().putIfAbsent(req.getRequestId(), new LinkedBlockingQueue<BabarResponse>(1));
		}
		super.write(ctx, msg, promise);
	}
}

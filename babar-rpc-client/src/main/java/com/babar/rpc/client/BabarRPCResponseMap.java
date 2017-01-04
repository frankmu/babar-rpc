package com.babar.rpc.client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.babar.rpc.common.BabarRPCResponse;

@Component
public class BabarRPCResponseMap {
	private ConcurrentHashMap<String, BlockingQueue<BabarRPCResponse>> responseMap;
	
	public BabarRPCResponseMap(){
		responseMap = new ConcurrentHashMap<String, BlockingQueue<BabarRPCResponse>>();
	}

	public ConcurrentHashMap<String, BlockingQueue<BabarRPCResponse>> getResponseMap() {
		return responseMap;
	}

	public void setResponseMap(ConcurrentHashMap<String, BlockingQueue<BabarRPCResponse>> responseMap) {
		this.responseMap = responseMap;
	}
}

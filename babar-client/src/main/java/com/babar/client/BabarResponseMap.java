package com.babar.client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.babar.common.BabarResponse;

@Component
public class BabarResponseMap {
	private ConcurrentHashMap<String, BlockingQueue<BabarResponse>> responseMap;
	
	public BabarResponseMap(){
		responseMap = new ConcurrentHashMap<String, BlockingQueue<BabarResponse>>();
	}

	public ConcurrentHashMap<String, BlockingQueue<BabarResponse>> getResponseMap() {
		return responseMap;
	}

	public void setResponseMap(ConcurrentHashMap<String, BlockingQueue<BabarResponse>> responseMap) {
		this.responseMap = responseMap;
	}
}

package com.babar.rpc.client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class BabarRPCResponseMap {
	private ConcurrentHashMap<String, CompletableFuture<Object>> responseMap;
	
	public BabarRPCResponseMap(){
		responseMap = new ConcurrentHashMap<String, CompletableFuture<Object>>();
	}

	public ConcurrentHashMap<String, CompletableFuture<Object>> getResponseMap() {
		return responseMap;
	}

	public void setResponseMap(ConcurrentHashMap<String, CompletableFuture<Object>> responseMap) {
		this.responseMap = responseMap;
	}
}

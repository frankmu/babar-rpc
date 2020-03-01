package com.babar.service.sample;

import java.util.concurrent.CompletableFuture;

public interface IBabarSampleService {
	
	public CompletableFuture<String> Hello(String name);
}

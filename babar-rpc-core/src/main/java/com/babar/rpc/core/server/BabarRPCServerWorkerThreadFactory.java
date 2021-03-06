package com.babar.rpc.core.server;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class BabarRPCServerWorkerThreadFactory implements ThreadFactory{

	private static final AtomicInteger factoryNum = new AtomicInteger(1);
	private final AtomicInteger threadNum = new AtomicInteger(1);
	private String prefix;
	
	public BabarRPCServerWorkerThreadFactory() {
		this.prefix = "babar-server-worker-" + factoryNum.getAndIncrement();
	}

	@Override
	public Thread newThread(Runnable runnable) {
		String threadName = this.prefix + "-thread-" + this.threadNum.getAndIncrement();
		return new Thread(runnable, threadName);
	}
}
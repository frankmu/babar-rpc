package com.babar.rpc.core.bootstrap;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.babar.rpc.core.config.BabarRPCConfig;

public class BabarRPCServerBootstrap {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		AnnotationConfigApplicationContext cxt = new AnnotationConfigApplicationContext(BabarRPCConfig.class);
		cxt.registerShutdownHook();
	}
}
package com.babar.core.bootstrap;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.babar.core.config.BabarConfig;

public class BabarServerBootstrap {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		AnnotationConfigApplicationContext cxt = new AnnotationConfigApplicationContext(BabarConfig.class);
		cxt.registerShutdownHook();
	}
}
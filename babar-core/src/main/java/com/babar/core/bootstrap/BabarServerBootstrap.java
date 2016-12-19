package com.babar.core.bootstrap;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.babar.core.config.BabarConfig;

public class BabarServerBootstrap {
	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(BabarConfig.class);
	}
}
package com.babar.client.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(value="com.babar.client")
@EnableAutoConfiguration
public class BabarClientAppliation {
	public static void main(String[] args) {
		SpringApplication.run(BabarClientAppliation.class, args);
	}
}
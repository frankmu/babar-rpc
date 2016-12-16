package com.babar.core.application;

import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(value="com.babar.core")
@EnableAutoConfiguration
public class BabarServerAppliation {
	public static void main(String[] args) throws UnknownHostException {
		SpringApplication.run(BabarServerAppliation.class, args);
	}
}
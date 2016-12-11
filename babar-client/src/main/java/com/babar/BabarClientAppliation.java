package com.babar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class BabarClientAppliation {
	public static void main(String[] args) {
		SpringApplication.run(BabarClientAppliation.class, args);
	}
}
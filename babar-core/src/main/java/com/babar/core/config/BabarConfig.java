package com.babar.core.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.babar.core", "com.babar.service"})
public class BabarConfig {

}

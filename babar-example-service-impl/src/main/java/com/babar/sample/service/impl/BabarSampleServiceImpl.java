package com.babar.sample.service.impl;

import org.springframework.stereotype.Component;

import com.babar.common.BabarService;
import com.babar.sample.service.IBabarSampleService;

@Component
@BabarService(value = IBabarSampleService.class)
public class BabarSampleServiceImpl implements IBabarSampleService{

	@Override
	public String Hello(String name) {
		return "Hello " + name;
	}

}

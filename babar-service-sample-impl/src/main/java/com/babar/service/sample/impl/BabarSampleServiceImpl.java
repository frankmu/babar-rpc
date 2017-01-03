package com.babar.service.sample.impl;

import org.springframework.stereotype.Component;

import com.babar.common.BabarService;
import com.babar.service.sample.IBabarSampleService;

@Component
@BabarService(value = IBabarSampleService.class)
public class BabarSampleServiceImpl implements IBabarSampleService{

	@Override
	public String Hello(String name) {
		try {
			Thread.sleep(1000);
			return "Hello " + name;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return "error";
		}
	}

}

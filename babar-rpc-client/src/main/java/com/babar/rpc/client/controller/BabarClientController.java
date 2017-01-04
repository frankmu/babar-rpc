package com.babar.rpc.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.babar.rpc.client.BabarRPCProxy;
import com.babar.service.sample.IBabarSampleService;;

@RestController
public class BabarClientController {
	
	@Autowired
	private BabarRPCProxy babarProxy;

	@RequestMapping(value="/hello", method=RequestMethod.GET)
	public String hello(@RequestParam(value="name", required=false, defaultValue="world")String name) {
		IBabarSampleService service = babarProxy.create(IBabarSampleService.class);
		return service.Hello(name);
	}
}
package com.babar.rpc.client.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;;

@RestController
public class BabarTestController {
	@RequestMapping("test")
	public String test(@RequestParam(value="name", required=false, defaultValue="world")String name) {
		return "Test " + name;
	}
}
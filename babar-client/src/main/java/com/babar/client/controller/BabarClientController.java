package com.babar.client.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;;

@RestController
public class BabarClientController {

	@RequestMapping(value="/hello", method=RequestMethod.GET)
	public String hello(@RequestParam(value="name", required=false, defaultValue="world")String name) {
		return "Hello " + name;
	}
}
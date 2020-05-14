package com.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigClientController {

	@Value("${port}")
	private String port;
	
	@RequestMapping("/port")
	public String hello() {
		return this.port;
	}

}

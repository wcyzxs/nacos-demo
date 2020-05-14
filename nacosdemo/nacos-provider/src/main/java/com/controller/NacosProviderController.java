package com.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NacosProviderController {
	
	
	@GetMapping("/helloNacos")
	public String helloNacos() {
		return "ÄãºÃ,nacos!";
	}
}

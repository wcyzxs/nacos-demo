package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.config.NacosFeignClient;

@RestController
public class FeignController {
	
	@Autowired
	private NacosFeignClient nacosFeignClient;
	
	@GetMapping("/feign")
	public String test() {
		return nacosFeignClient.helloNacos();
	}
	
}

package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class NacosConsumerController {

	private String url = "http://nacos-provider";   //�ṩ����΢��������
	
	@Autowired
	RestTemplate restTemplate;
	
	@GetMapping
	public String test() {
		return restTemplate.getForObject(url+"/helloNacos", String.class);
		
	}
}

package com.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import entity.Dept;

@RestController
public class ProviderController {

	@Value("${server.port}")
	private Integer port ;
	
	@GetMapping("/hello")
	public String say(@RequestParam String id) {
		int a = 1/0;
		System.out.println("调用8000服务下的方法");
		return "hello provider--->" + port+"----"+id;
	}
	
	@PostMapping("/save")
	public String post(@RequestBody Dept dept) {
		//int a = 1/0;
		System.out.println("调用8000服务下的post方法"+dept.toString());
		return "hello provider--->" + port+"----"+dept.toString();
	}
}

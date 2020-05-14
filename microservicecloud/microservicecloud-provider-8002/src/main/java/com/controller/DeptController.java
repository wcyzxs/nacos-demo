package com.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeptController {

	@Value("${server.port}")
	private Integer port ;
	
	@GetMapping("/hello")
	public String say(@RequestParam String id) {
		System.out.println("调用8002服务下的方法");
		return "hello provider--->" + port+"---"+id;
	}
}

package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.feign.ServiceFeginClient;

import entity.Dept;


@RestController
public class ConsumerController {
	
	@Autowired
	DiscoveryClient discoveryClient;
	
	@Autowired
	ServiceFeginClient serviceFeginClient;
	
	@Autowired
	RestTemplate restTemplate;
	
//	@GetMapping("/hello")
//	@HystrixCommand(fallbackMethod = "hiFallback")  //调用服务方方法后的回调
//	public String hello(@RequestParam String id) {
//		String s =null;
//		s=restTemplate.getForObject("http://provider8000/hello?id="+id, String.class);
//		System.out.println(s);
//		return s;
//	}

	public String hiFallback(String id) {
		return "hi,"+id+",error";
	}

	@RequestMapping("/hello")
    public String hi(@RequestParam String id){
		String s = null;
		for(int i=0;i<30;i++) {
			System.out.println("***********"+id);
	       s = serviceFeginClient.hi(id);
		}
		  return s;
    }
	
	@PostMapping("/save")
    public String save(@RequestBody Dept dept){
		System.out.println("***********"+dept.toString());
        return serviceFeginClient.save(dept);
    }
}


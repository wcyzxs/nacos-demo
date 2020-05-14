package com.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fallback.ServiceAFeignClientFallback;

import entity.Dept;


@FeignClient(value = "provider8000",fallback = ServiceAFeignClientFallback.class)   //服务方的服务名称
public interface ServiceFeginClient {

	@RequestMapping(value = "/hello")   //该抽象方法的注解、访问路径、方法签名要和提供服务的方法完全一致
	String hi(@RequestParam("id") String id);
	
	@PostMapping(value = "/save")   
	String save(@RequestBody Dept dept);
}

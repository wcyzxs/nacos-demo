package com.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="nacos-provider" ,fallback = RemoteFeign.class)
public interface NacosFeignClient {
	
	@GetMapping("/helloNacos")
	String helloNacos();
}

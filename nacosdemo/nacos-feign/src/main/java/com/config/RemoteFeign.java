package com.config;

import org.springframework.stereotype.Component;

@Component
public class RemoteFeign implements NacosFeignClient {

	public String helloNacos() {
		return "«Î«Û≥¨ ±";
	}

}

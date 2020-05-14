package com.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.feign.ServiceFeginClient;

import entity.Dept;
import feign.hystrix.FallbackFactory;


@Component
public class ServiceAFeignClientFallback implements ServiceFeginClient{
	private static final Logger log = LoggerFactory.getLogger(ServiceAFeignClientFallback.class);
	
	/**
	 * 这种情况下，直接implements ServiceFeginClient即可
	 * @param id
	 * @return
	 */
	@Override
	public String hi(String id) {
		return "hello---"+id+"----"+"error";
	}

	@Override
	public String save(Dept dept) {
		return "save方法"+dept.toString()+"---------"+"error";
	}

	/**
	 * 需要打印日志的时候，需要这样implements FallbackFactory<ServiceFeginClient>
	 */
//	@Override
//	public ServiceFeginClient create(Throwable cause) {
//		return new ServiceFeginClient() {			
//			@Override
//			public String hi(String id) {
//				ServiceAFeignClientFallback.log.info("fallback reson was---->>>"+cause);
//				return "hello---"+id+"----"+"error";
//			}
//		};
//	}
}

package com.appdirect.service.ping;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.appdirect.appdirect.service.feign.CustomErrorDecoder;
import feign.codec.ErrorDecoder;

/**
 * Override some of the default configuration for ALL Feign clients.
 *
 * @author Dan Nathanson
 */
@Configuration
public class FeignConfiguration {
	@Bean
	ErrorDecoder errorDecoder() {
		return new CustomErrorDecoder();
	}
}

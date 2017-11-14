package com.appdirect.appdirect.service.feign;

import static feign.FeignException.errorStatus;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;

/**
 * Feign ErrorDecoder that recognizes 50x errors and raises them as RetryableExceptions.
 *
 * @author Dan Nathanson
 */
public class CustomErrorDecoder implements ErrorDecoder {
	@Override
	public Exception decode(String methodKey, Response response) {
		FeignException exception = errorStatus(methodKey, response);
		if (response.status() >= 500) {
			return new RetryableException(exception.getMessage(), exception, null);
		}
		return exception;
	}
}

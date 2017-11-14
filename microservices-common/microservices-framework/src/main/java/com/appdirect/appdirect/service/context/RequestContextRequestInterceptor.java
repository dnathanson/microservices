package com.appdirect.appdirect.service.context;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Feign {@link RequestInterceptor} that adds JSON-serialized AppDirectRequestContext to request headers.  It gets the
 * context from Hystrix ContextVariable.
 * <p>
 * TODO: Do we need to add logic to ensure that context not added to requests going OUTSIDE our network?
 *
 * @author Dan Nathanson
 */
@Slf4j
public class RequestContextRequestInterceptor implements RequestInterceptor {
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void apply(RequestTemplate requestTemplate) {
		AppDirectRequestContext context = AppDirectRequestContextHystrixContextVariable.getInstance().get();
		String contextJson = null;
		if (context != null) {
			try {
				contextJson = objectMapper.writeValueAsString(context);
			} catch (JsonProcessingException e) {
				log.error("Error serializing request context to JSON", e);
			}
		}

		requestTemplate.header(AppDirectRequestContext.HTTP_HEADER_NAME, contextJson);
	}
}

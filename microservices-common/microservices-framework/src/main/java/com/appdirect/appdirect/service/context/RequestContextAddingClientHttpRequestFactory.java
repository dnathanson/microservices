package com.appdirect.appdirect.service.context;

import java.io.IOException;
import java.net.URI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.ribbon.RibbonClientHttpRequestFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;

/**
 * Replacement for Spring's standard ClientHttpRequestFactory implementation that is plugged into RestTemplate.
 * This implementation automatically adds {@link AppDirectRequestContext} to outgoing HTTP requests. It gets the
 * context from Hystrix ContextVariable.
 * <p>
 * TODO: Do we need to add logic to ensure that context not added to requests going OUTSIDE our network?
 *
 * @author Dan Nathanson
 */
@Slf4j
public class RequestContextAddingClientHttpRequestFactory extends RibbonClientHttpRequestFactory {
	private ObjectMapper objectMapper = new ObjectMapper();

	public RequestContextAddingClientHttpRequestFactory(SpringClientFactory clientFactory) {
		super(clientFactory);
	}

	@Override
	public ClientHttpRequest createRequest(URI url, HttpMethod method) throws IOException {
		ClientHttpRequest request = super.createRequest(url, method);
		AppDirectRequestContext context = AppDirectRequestContextHystrixContextVariable.getInstance().get();
		String contextJson = null;
		if (context != null) {
			try {
				contextJson = objectMapper.writeValueAsString(context);
			} catch (JsonProcessingException e) {
				log.error("Error serializing request context to JSON", e);
			}
		}

		request.getHeaders().add(AppDirectRequestContext.HTTP_HEADER_NAME, contextJson);
		return request;
	}
}

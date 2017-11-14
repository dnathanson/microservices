package com.appdirect.appdirect.service.context;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;

/**
 * Copies context from HTTP request headers into AppDirectContext Hystrix variable.
 *
 * @author Dan Nathanson
 */
@Slf4j
public class AppDirectRequestContextFilter extends OncePerRequestFilter {
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HystrixRequestVariableDefault<AppDirectRequestContext> variable = AppDirectRequestContextHystrixContextVariable.getInstance();
		String contextJson = request.getHeader(AppDirectRequestContext.HTTP_HEADER_NAME);
		if (contextJson != null) {
			AppDirectRequestContext requestContext = objectMapper.readValue(contextJson, AppDirectRequestContext.class);
			String sessionId = (String) requestContext.getValues().get(AppDirectRequestContext.SESSION_ID_NAME);
			String requestId = (String) requestContext.getValues().get(AppDirectRequestContext.REQUEST_ID_NAME);
			MDC.put(AppDirectRequestContext.SESSION_ID_NAME, sessionId);
			MDC.put(AppDirectRequestContext.REQUEST_ID_NAME, requestId);
			variable.set(requestContext);
		}

		filterChain.doFilter(request, response);

		MDC.remove(AppDirectRequestContext.SESSION_ID_NAME);
		MDC.remove(AppDirectRequestContext.REQUEST_ID_NAME);
	}

}

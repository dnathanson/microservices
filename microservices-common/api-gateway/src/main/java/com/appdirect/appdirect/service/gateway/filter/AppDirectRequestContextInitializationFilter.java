package com.appdirect.appdirect.service.gateway.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import com.appdirect.appdirect.service.context.AppDirectRequestContext;
import com.appdirect.appdirect.service.context.AppDirectRequestContextHystrixContextVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;

/**
 * Example of how context / state can be managed client-side by passing session ID in cookie.  Using session ID,
 * context for session can be loaded from
 *
 * @author Dan Nathanson.
 */
@Slf4j
public class AppDirectRequestContextInitializationFilter extends OncePerRequestFilter {

	public static final String COOKIE_NAME = "POC_SESSION";

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HystrixRequestVariableDefault<AppDirectRequestContext> variable = AppDirectRequestContextHystrixContextVariable.getInstance();
		AppDirectRequestContext requestContext = new AppDirectRequestContext();
		String sessionId = Optional.ofNullable(request.getCookies())
				.flatMap(cookies -> Arrays.stream(cookies)
						.filter(cookie -> cookie.getName().equals(COOKIE_NAME))
						.map(Cookie::getValue)
						.findFirst())
				.orElse(RandomStringUtils.randomAlphanumeric(16));

//	    String sessionId = RandomStringUtils.randomAlphanumeric(16);
		requestContext.addToContext(AppDirectRequestContext.SESSION_ID_NAME, sessionId.substring(0, 10));

		// Create a random request ID.  This can be used for log correlation.
		String requestId = RandomStringUtils.randomAlphanumeric(16);
		requestContext.addToContext(AppDirectRequestContext.REQUEST_ID_NAME, requestId);
		variable.set(requestContext);

		MDC.put(AppDirectRequestContext.SESSION_ID_NAME, sessionId.substring(0, 10));
		MDC.put(AppDirectRequestContext.REQUEST_ID_NAME, requestId);

		log.info("Adding Session ID {} to request context", sessionId);
		response.addCookie(new Cookie(COOKIE_NAME, sessionId));
		filterChain.doFilter(request, response);

		MDC.clear();
	}
}

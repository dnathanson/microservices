package com.appdirect.appdirect.service.context;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.filter.OncePerRequestFilter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

/**
 * Add support for Context that is automatically passed to threads that Hystrix uses for making async requests.
 *
 * @author Dan Nathanson
 */
@Slf4j
public class HystrixRequestContextFilter extends OncePerRequestFilter {


	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HystrixRequestContext context = HystrixRequestContext.initializeContext();
		try {
			filterChain.doFilter(request, response);
		} finally {
			// When using DeferredResult, we cannot shut down context
			if (!isAsyncStarted(request)) {
				context.shutdown();
			}
		}
	}
}

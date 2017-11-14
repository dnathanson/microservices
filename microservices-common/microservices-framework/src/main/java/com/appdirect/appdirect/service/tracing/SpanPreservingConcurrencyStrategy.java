package com.appdirect.appdirect.service.tracing;

import java.util.concurrent.Callable;

import org.slf4j.MDC;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceKeys;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixConcurrencyStrategy;

import com.appdirect.appdirect.service.context.AppDirectRequestContext;

/**
 * HystrixConcurrencyStrategy that both propagates trace information into child threads, but also sets logging
 * diagnostic context on child threads.
 */
public class SpanPreservingConcurrencyStrategy extends SleuthHystrixConcurrencyStrategy {
	private Tracer tracer;
	private TraceKeys traceKeys;
	private static final String HYSTRIX_COMPONENT = "hystrix";

	public SpanPreservingConcurrencyStrategy(Tracer tracer, TraceKeys traceKeys) {
		super(tracer, traceKeys);
		this.tracer = tracer;
		this.traceKeys = traceKeys;
	}

	@Override
	public <T> Callable<T> wrapCallable(Callable<T> callable) {
		return new HystrixTraceCallable<>(this.tracer, this.traceKeys, callable);
	}

	/**
	 * Callable used to wrap main Hystrix callable, but adds tracing and diagnostic context.
	 * <p>
	 * Note: This implementation is copied from SleuthHystrixConcurrencyStrategy with MDC setup added. Would have
	 * been nice to be able to extend that one, but it is private in that class.
	 *
	 * @param <S>
	 */
	private static class HystrixTraceCallable<S> implements Callable<S> {

		private Tracer tracer;
		private TraceKeys traceKeys;
		private Callable<S> callable;
		private Span parent;
		private String requestId;
		private String sessionId;

		public HystrixTraceCallable(Tracer tracer, TraceKeys traceKeys, Callable<S> callable) {
			this.tracer = tracer;
			this.traceKeys = traceKeys;
			this.callable = callable;
			this.parent = tracer.getCurrentSpan();
			this.sessionId = MDC.get(AppDirectRequestContext.SESSION_ID_NAME);
			this.requestId = MDC.get(AppDirectRequestContext.REQUEST_ID_NAME);
		}

		@Override
		public S call() throws Exception {
			MDC.put(AppDirectRequestContext.REQUEST_ID_NAME, requestId);
			MDC.put(AppDirectRequestContext.SESSION_ID_NAME, sessionId);
			Span span = this.parent;
			boolean created = false;
			if (span != null) {
				span = this.tracer.continueSpan(span);
			} else {
				span = this.tracer.createSpan(HYSTRIX_COMPONENT);
				this.tracer.addTag(Span.SPAN_LOCAL_COMPONENT_TAG_NAME, HYSTRIX_COMPONENT);
				this.tracer.addTag(this.traceKeys.getAsync().getPrefix() +
						this.traceKeys.getAsync().getThreadNameKey(), Thread.currentThread().getName());
				created = true;
			}
			try {
				return this.callable.call();
			} finally {
				if (created) {
					this.tracer.close(span);
				} else {
					this.tracer.detach(span);
				}
				MDC.remove(AppDirectRequestContext.REQUEST_ID_NAME);
				MDC.remove(AppDirectRequestContext.SESSION_ID_NAME);
			}
		}

	}
}

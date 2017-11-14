package com.appdirect.appdirect.service.context;


import java.util.HashMap;
import java.util.Map;


/**
 * Request context that is passed from service to service using HTTP headers. Context is serialized to JSON and
 * pass as a single header value.
 * <p>
 * Since this object will be serialized/deserialized by services that may be running different versions of this
 * library, it is <strong>imperative</strong> that any changes to this class are backwards compatible from a
 * JSON serialization perspective.
 *
 * @author Dan Nathanson.
 */
public class AppDirectRequestContext {

	public static final String HTTP_HEADER_NAME = "AD_CONTEXT";
	public static final String REQUEST_ID_NAME = "REQUEST_ID";
	public static final String SESSION_ID_NAME = "SESSION_ID";

	// For now, simple map to hold all context values.  May want to promote some values to
	private Map<String, Object> values = new HashMap<>();

	public void addToContext(String name, Object value) {
		values.put(name, value);
	}

	public Map<String, Object> getValues() {
		return values;
	}

	public void setValues(Map<String, Object> values) {
		this.values = values;
	}
}

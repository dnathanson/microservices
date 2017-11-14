package com.appdirect.service.pong.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Response from /gnop endpoint of Pong Service.
 *
 * @author Dan Nathanson.
 */
@Getter
@Setter
public class GnopResponse {
	private long requestId;
	private String message;
}

package com.appdirect.service.pong.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response from /pong endpoint of Pong Service.
 *
 * @author Dan Nathanson.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PongResponse {
	private long requestId;
	private String message;
}

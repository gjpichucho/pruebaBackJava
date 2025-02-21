package com.nttdata.accountservice.infrastructure.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ModelNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String message_debug;
	private HttpStatus status;

	public ModelNotFoundException(String mensaje, String message_debug, HttpStatus status) {
		super(mensaje);
		this.status = status;
		this.message_debug = message_debug;
	}
}

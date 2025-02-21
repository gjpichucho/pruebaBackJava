package com.nttdata.clientservice.infrastructure.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DataAccessCustomException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messageDebug;
	private HttpStatus status;

	public DataAccessCustomException(String message, String _messageDebug, HttpStatus status) {
		super(message);
		this.messageDebug = _messageDebug;
		this.status = status;
	}

}

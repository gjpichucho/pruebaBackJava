package com.nttdata.clientservice.infrastructure.exception;

import com.nttdata.clientservice.application.config.LoggerService;
import com.nttdata.clientservice.infrastructure.enums.LoggerCustomType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * 
 * @author 
 * @apiNote excepción lanzada si rompe con las reglas de negocio
 */
@Getter
public class BusinessLogicException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String messageDebug;
	private HttpStatus status;
	
	@Autowired
	private LoggerService logger;

	public BusinessLogicException(String message, String _messageDebug) {
		super(message);
		this.messageDebug = _messageDebug;
	}

	public BusinessLogicException(String codeError, String msgError, String msgDebug, String classNameThrowable,
			String actionThrowable) {
		super(msgError);

		if (logger == null) {
			logger = new LoggerService();
		}

		logger.addAction(actionThrowable)
				.addClassName(classNameThrowable)
				.addCodeStatus(codeError)
				.addExceptionMsg(msgDebug)
				.addMsgUser(msgError)
				.printLog(LoggerCustomType.ERROR);

		this.messageDebug = msgDebug;

	}

	public BusinessLogicException(String codeError, String msgError, Throwable ex) {
		super();

		if (logger == null) {
			logger = new LoggerService();
		}

		logger.addCodeStatus(codeError)
				.addMsgUser(msgError)
				.addExceptionMsg(ex.getMessage())
				.printLog(LoggerCustomType.ERROR);

		this.messageDebug = msgError;

	}

}

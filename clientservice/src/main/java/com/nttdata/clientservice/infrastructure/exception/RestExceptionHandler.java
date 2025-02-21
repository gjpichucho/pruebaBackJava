package com.nttdata.clientservice.infrastructure.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler {


	/**
	 * Handle DataIntegrityViolationException, inspects the cause for different DB
	 * causes.
	 *
	 * @param ex the DataIntegrityViolationException
	 * @return the ApiError object
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
			WebRequest request) {
		if (ex.getCause() instanceof ConstraintViolationException) {
			return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Database error", ex.getCause()));
		}
		return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
	}

	/**
	 * Handle Exception, handle generic Exception.class
	 *
	 * @param ex the Exception
	 * @return the ApiError object
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
		apiError.setMessageDebug(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@ExceptionHandler(BusinessLogicException.class)
	public ResponseEntity<Object> handlerBusinessLogicException(BusinessLogicException ex) {
		HttpStatus status = ex.getStatus() != null ? ex.getStatus() : HttpStatus.BAD_REQUEST;
		ApiError apiError = new ApiError(status);
		apiError.setMessage(ex.getMessage());
		apiError.setMessageDebug(ex.getMessageDebug());
		return this.buildResponseEntity(apiError);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handlerIllegalArgumentException(IllegalArgumentException ex) {
		ApiError apiError = new ApiError(ex.getMessage(), "IllegalArgumentException", HttpStatus.CONFLICT);
		return this.buildResponseEntity(apiError);
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<Object> handlerDataAccessCustomException(DataAccessException ex) {
		ApiError apiError = new ApiError("Internal Error", "Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);
		log.error("Data Access Exception:" + ex.getMessage());
		return this.buildResponseEntity(apiError);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
			WebRequest request) {
		ApiError apiError = new ApiError(ex.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST);

		return this.buildResponseEntity(apiError);

	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public final ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage("Tamaño máximo de carga excedido. Límite máximo 2MB");
		apiError.setMessageDebug(ex.getMostSpecificCause().getLocalizedMessage());
		log.error("Tamaño máximo de carga excedido. Límite máximo 2MB");
		return this.buildResponseEntity(apiError);

	}

	@ExceptionHandler(ModelNotFoundException.class)
	public final ResponseEntity<Object> handleModelNotFoundExceptionn(ModelNotFoundException ex) {
		HttpStatus status = ex.getStatus() != null ? ex.getStatus() : HttpStatus.NOT_FOUND;
		ApiError apiError = new ApiError(ex.getMessage(), ex.getMessage_debug(), status);

		return this.buildResponseEntity(apiError);

	}
	
	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<Object> handleNotFoundExceptionn(NotFoundException ex) {
		HttpStatus status = ex.getStatus() != null ? ex.getStatus() : HttpStatus.NOT_FOUND;
		ApiError apiError = new ApiError(ex.getMessage(), ex.getMessageDebug(), status);

		return this.buildResponseEntity(apiError);

	}

}

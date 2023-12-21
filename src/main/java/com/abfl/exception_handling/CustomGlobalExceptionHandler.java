package com.abfl.exception_handling;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public final ResponseEntity<CustomExceptionSchema> handleAllExceptions(CustomException ex) {
		CustomExceptionSchema exceptionResponse = new CustomExceptionSchema();
		exceptionResponse.setHttpStatus(ex.getHttpStatus());
		exceptionResponse.setStatus(String.valueOf(ex.getHttpStatus().value()));

		exceptionResponse.setMessage(ex.getMessage());

		return new ResponseEntity<>(exceptionResponse, ex.getHttpStatus());
	}

}

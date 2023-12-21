package com.abfl.exception_handling;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class CustomException extends Throwable {
	private final HttpStatus httpStatus;

	private final int status;

	private final String message;

	public CustomException(CustomException ex) {

		this.status = ex.status;
		this.message = ex.message;
		this.httpStatus = ex.httpStatus;

	}

	public CustomException(CustomException ex, HttpStatus status, String message) {

		this.httpStatus = status;
		this.status = status.value();
		this.message = message;

	}

}

package com.abfl.exception_handling;

import javax.servlet.ServletException;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CustomException2 extends ServletException {
	private final HttpStatus statusCode;
	private final String statusMessage;
	private final String exceptionMessage;
	private final String methodName;

	public CustomException2(CustomException2 ex) {

		this.statusCode = ex.statusCode;
		this.statusMessage = ex.statusMessage;
		this.exceptionMessage = ex.exceptionMessage;
		this.methodName = ex.methodName;
	}

	public CustomException2(CustomException2 ex, HttpStatus statusCode, String statusMessage) {

		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.exceptionMessage = ex.getMessage();
		this.methodName = "";
	}

}

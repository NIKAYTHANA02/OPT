package com.abfl.exception_handling;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomExceptionSchema {

	private HttpStatus httpStatus;

	private String status;

	private String message;
}

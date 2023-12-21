package com.abfl.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.abfl.data_objects.OTPValidationInputDo;
import com.abfl.exception_handling.CustomException;

@Service
public interface OTPValidationService {

	public ResponseEntity<?> validateOtP(OTPValidationInputDo oTPValidationInputDo) throws CustomException;
}

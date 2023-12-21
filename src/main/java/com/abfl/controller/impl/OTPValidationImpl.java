package com.abfl.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.abfl.controller.OTPValidation;
import com.abfl.data_objects.OTPValidationInputDo;
import com.abfl.exception_handling.CustomException;
import com.abfl.service.OTPValidationService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "OTP Validation Controller", description = "")
@RestController
@CrossOrigin("*")
public class OTPValidationImpl implements OTPValidation {

	@Autowired
	OTPValidationService oTPValidationService;

	@Override
	public ResponseEntity<?> otpValidation(OTPValidationInputDo otpValidationInputDo) throws CustomException {

		return oTPValidationService.validateOtP(otpValidationInputDo);
	}

}

package com.abfl.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.abfl.controller.TestController;
import com.abfl.data_objects.SmsApiResponse;
import com.abfl.data_objects.abfl.OtpSmsObj;
import com.abfl.service.OtpService;
import com.abfl.service.impl.OtpServiceImpl;

@RestController
@CrossOrigin
public class TestkControllerImpl implements TestController {

	@Autowired
	OtpService otpService;

	@Autowired
	OtpServiceImpl otpServiceImpl;

	@Override
	public String check() {
		return "hello it is working Fine";
	}

	@PostMapping(value = "/sendSmsApi")
	public ResponseEntity<SmsApiResponse> sendSmsApi(@RequestBody OtpSmsObj otpSmsObj) {
		return otpService.callSmsSendApi(otpSmsObj);
	}

	@GetMapping(value = "/sendsmsTest")
	public String sendsmsTest() {
		return otpServiceImpl.sendsmsTest();
	}

}

package com.abfl.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.abfl.data_objects.SmsApiResponse;
import com.abfl.data_objects.abfl.OtpSmsObj;
import com.abfl.exception_handling.CustomException;

@Service
public interface OtpService {

	public ResponseEntity<?> sendSms(String otp, String mobileNumber, String smsInd) throws CustomException;

	public ResponseEntity<SmsApiResponse> callSmsSendApi(OtpSmsObj otpSmsObj);

}

package com.abfl.controller.impl;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.abfl.controller.OTPGeneration;
import com.abfl.data_objects.OTPGenerationInputDo;
import com.abfl.data_objects.ResponseTemplate;
import com.abfl.exception_handling.CustomException;
import com.abfl.repository.CustomerDetailsRepo;
import com.abfl.service.ConfigService;
import com.abfl.service.OTPGenerationService;
import com.abfl.service.TrnLogService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "OTP Generation Controller", description = "")
@RestController
@CrossOrigin("*")
@Slf4j
public class OTPGenerationImpl implements OTPGeneration {

	@Autowired
	OTPGenerationService otpGenerationService;

	@Autowired
	CustomerDetailsRepo customerDetailsRepo;

	@Autowired
	TrnLogService trnLogService;

	@Autowired
	ConfigService configService;
	
	@Override
	public ResponseEntity<?> otpGeneration(OTPGenerationInputDo otpGenerationInputDo) throws CustomException {
		log.info("*****Starting OTP Generation*****");
		String strReqJsonBody = new JSONObject(otpGenerationInputDo).toString();
		Long lng = trnLogService.saveApiRequest("OTP-Service/otpGeneration", strReqJsonBody);

		String start = configService.getConfigValueById(48);
    	String end = configService.getConfigValueById(49);
		if (start != null && end != null) {
			LocalDateTime startTime = LocalDateTime.parse(start);
			LocalDateTime endTime = LocalDateTime.parse(end);
			LocalDateTime currentTime = LocalDateTime.now();
			LocalDateTime startTimeIST = startTime.plusHours(5).plusMinutes(30);
			LocalDateTime endTimeIST = endTime.plusHours(5).plusMinutes(30);
			String startHour = startTimeIST.getHour() > 9 ? String.valueOf(startTimeIST.getHour())
					: "0" +String.valueOf(startTimeIST.getHour());
			String startMinute = startTimeIST.getMinute() > 9 ? String.valueOf(startTimeIST.getMinute())
					: "0" +String.valueOf(startTimeIST.getMinute());
			String endHour = endTimeIST.getHour() > 9 ? String.valueOf(endTimeIST.getHour())
					: "0" +String.valueOf(endTimeIST.getHour());
			String endMinute = endTimeIST.getMinute() > 9 ? String.valueOf(endTimeIST.getMinute())
					: "0" +String.valueOf(endTimeIST.getMinute());
			System.out.println(currentTime);
			if (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
				String a = startTimeIST.getMonth() + " " + startTimeIST.getDayOfMonth() + "," + startTimeIST.getYear() + " "
						+ startHour + ":" + startMinute;
				String b = endTimeIST.getMonth() + " " + endTimeIST.getDayOfMonth() + "," + endTimeIST.getYear() + " "
						+ endHour + ":" + endMinute;
				String message = "Dear Customer, our system will not be available " + "between " + a + " and " + b
						+ " due to maintenance activity. Sorry for the inconvenience.";
				log.info("Downtime");
				trnLogService.saveApiErrorResponse(lng, message);
				ResponseTemplate responseTemplate = ResponseTemplate.builder().status("307").message(message)
						.build();
				return ResponseEntity.status(HttpStatus.OK).body(responseTemplate);
			}
		}
		String endLogMsg = "***** End of OTP Generation*****";
		if (otpGenerationInputDo.getIsExistingCustomer() != null
				&& otpGenerationInputDo.getIsExistingCustomer().equals(false)) {
			log.info("Not an existing customer");
			ResponseEntity<?> response = otpGenerationService.generateOtP(otpGenerationInputDo);
			trnLogService.saveApiResponse(lng, "OTP sent successfully");
			log.info(endLogMsg);
			return response;
		}
		
		String optTypeInd = otpGenerationInputDo.getOptTypeInd();
		String blockedInd = otpGenerationInputDo.getBlockedInd();
		log.info("OTP Ind : " + optTypeInd);
		String message = null;
		ResponseEntity<?> response = null;
		if (blockedInd.equals("Y")) {
			customerDetailsRepo.updateBlockedIndicator(blockedInd, otpGenerationInputDo.getCustId());
			message = "Your Account has been blocked. Please reset Mpin";

			log.info("Your Account has been blocked. Please reset Mpin");
			trnLogService.saveApiErrorResponse(lng, message);
			throw CustomException.builder().httpStatus(HttpStatus.EXPECTATION_FAILED).message(message).build();

		} else {

			message = otpGenerationService.validateMobileNumber(otpGenerationInputDo);

			if (message != null && message.equals("SUCCESS")) {
				response = otpGenerationService.generateOtP(otpGenerationInputDo);

			} else {
				trnLogService.saveApiErrorResponse(lng, message);
				throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message(message).build();
			}

		}
		trnLogService.saveApiResponse(lng, "OTP sent successfully");
		log.info(endLogMsg);
		return response;
	}

}

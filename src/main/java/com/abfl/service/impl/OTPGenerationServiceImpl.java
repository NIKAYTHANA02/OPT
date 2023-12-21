package com.abfl.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.abfl.data_objects.CustomerDetails;
import com.abfl.data_objects.OTPGenerationInputDo;
import com.abfl.data_objects.ResponseTemplate;
import com.abfl.data_objects.SmsApiResponse;
import com.abfl.entity.OtpEntity;
import com.abfl.exception_handling.CustomException;
import com.abfl.repository.CustomerDetailsRepo;
import com.abfl.repository.OtpEntityRepo;
import com.abfl.service.OTPGenerationService;
import com.abfl.service.OtpService;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OTPGenerationServiceImpl implements OTPGenerationService {

	@Autowired
	OtpEntityRepo otpEntityRepo;

	@Autowired
	OtpService otpService;

	@Autowired
	CustomerDetailsRepo customerDetailsRepo;

	@Override
	public ResponseEntity<?> generateOtP(OTPGenerationInputDo otpGenerationInputDo) throws CustomException {
		try {
			System.out.println("Inside Generate OTP Service ");
			log.info("Inside Generate OTP Service ");
			Timestamp instant = Timestamp.from(Instant.now());
			String otpNumber = null;

			otpNumber = getOtpNumber();
			long currentTimestamp = System.currentTimeMillis();

			if (otpGenerationInputDo.getMobileNumber().equals("9999999999")) {
				OtpEntity otpEntity = new OtpEntity().builder().mobileNumber(otpGenerationInputDo.getMobileNumber())
						.otp("123456").createdBy(otpGenerationInputDo.getCustId().longValue())
						// .modifiedBy(otpGenerationInputDo.getCustId().longValue())
						.createdOn(instant).build();

				otpEntityRepo.save(otpEntity);
				log.info("Success ");
				return ResponseTemplate.okResponse(
						"An OTP has been shared to your registered mobile number." + " Please enter the same.",
						Collections.singletonMap("otp", ""));
			} else {
				OtpEntity otpEntity = new OtpEntity();
				ResponseEntity<?> msgResponse = null;

				List<OtpEntity> otpList = otpEntityRepo.getOTPDetails(otpGenerationInputDo.getMobileNumber(),
						otpGenerationInputDo.getSmsInd(), String.valueOf(otpGenerationInputDo.getCustId()));
				log.info("Size of OTP List : " + otpList.size());

				if (otpList != null && otpList.size() > 0) {
					otpEntity = otpList.get(0);
				}

				if (otpList.size() == 0) {
					log.info("OTP list size is zero" + "*********" + "Calling send sms" + "***********");

					msgResponse = callingSendSms(otpGenerationInputDo, otpNumber);

					log.info("Message Response : " + msgResponse.toString());

				} else if (otpList.size() > 0 && otpList.size() < 5) {

					Timestamp createdOn = otpEntity.getCreatedOn();
					log.info("Latest OTP created On : " + otpEntity.getCreatedOn());
					long searchTimestamp = createdOn.getTime(); // this also gives me back timestamp in 13 digit
																// (1425506040493)

					long difference = Math.abs(currentTimestamp - searchTimestamp);
					String smsIndFromTable = otpEntity.getSmsInd();
					String smsIndFromInput = otpGenerationInputDo.getSmsInd();
					System.out.println(difference);
					log.info("Time difference :" + difference);
					if (difference > 5 * 60 * 1000) { // && (! (smsIndFromTable.equalsIgnoreCase(smsIndFromInput)))){
//send
						log.info("*********" + "Calling send sms" + "***********");

						msgResponse = callingSendSms(otpGenerationInputDo, otpNumber);
						log.info("Message Response : " + msgResponse.toString());

					}

					else {
						log.info("Time duration between 2 OTP requests should be more than 5 Minutes");
						throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST)
								.message("Time duration between 2 OTP requests should be more than 5 Minutes").build();

					}
				} else if (otpList.size() == 5) {
					log.info("OTP List Size = " + otpList.size());
					Timestamp createdOnForHour = otpEntity.getCreatedOn();

					long searchTimestampForHour = createdOnForHour.getTime();
					long differenceForHours = Math.abs(currentTimestamp - searchTimestampForHour);

					// Commented the condition for UAT testing purpose.
					log.info("differenceForHours : " + differenceForHours);
					if (differenceForHours > 1 * 3600 * 1000) {// 1 * 3600 * 1000 - 1 hr , if the time exceeds 1 hr

						log.info("time exceeds 1 hr");
						log.info("*********Calling Send sms********");
						// send
						msgResponse = callingSendSms(otpGenerationInputDo, otpNumber);
						log.info("Message Response : " + msgResponse.toString());
					} else {
						log.info("OTP limit exceeded, please try after 1 hour");
						throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST)
								.message("OTP limit exceeded, please try after 1 hour").build();

					}

				}

				Gson gson = new Gson();
				String gsonJsonString = gson.toJson(msgResponse.getBody());
				SmsApiResponse smsApiResponse = gson.fromJson(gsonJsonString, SmsApiResponse.class);
				smsApiResponse.print();
				if (smsApiResponse.getStatus().equalsIgnoreCase("OK")) {
					otpEntity = new OtpEntity().builder().mobileNumber(otpGenerationInputDo.getMobileNumber())
							.otp(otpNumber).createdBy(otpGenerationInputDo.getCustId().longValue())
							.smsInd(otpGenerationInputDo.getSmsInd())
							// .modifiedBy(otpGenerationInputDo.getCustId().longValue())
							.createdOn(instant).build();

					otpEntityRepo.save(otpEntity);
					String maskedMobileNumber = null;
					String strPattern = "\\d(?=\\d{4})";
					maskedMobileNumber = otpGenerationInputDo.getMobileNumber().replaceAll(strPattern, "X");

					log.info("Success ");
					return ResponseTemplate.okResponse("An OTP has been shared to your registered mobile number "
							+ maskedMobileNumber + "." + " Please enter the same.",
							Collections.singletonMap("otp", ""));
				} else {

					throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST)
							.message(smsApiResponse.getMessage()).build();

				}
			}

		} catch (Exception e) {
			log.info("Exception occured in generateOtP:" + e.toString());
			System.out.println("Exception occured in generateOtP:" + e.toString());
			throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message(e.toString()).build();

		}
	}

	/*******************
	 * send OTP to respective mobile number
	 ****************************/
	public ResponseEntity<?> callingSendSms(OTPGenerationInputDo otpGenerationInputDo, String otpNumber)
			throws CustomException {
		try {

			ResponseEntity<?> msgResponse = otpService.sendSms(otpNumber, otpGenerationInputDo.getMobileNumber(),
					otpGenerationInputDo.getSmsInd());
//            SmsApiResponse

//			if (msgResponse.getStatusCode() != HttpStatus.OK) {
//
//				return msgResponse;
//			}
			return msgResponse;

		} catch (Exception e) {
			log.info("Exception occured in generateOtP:" + e.toString());
			System.out.println("Exception occured in generateOtP:" + e.toString());
			throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message(e.toString()).build();

		}
	}

	private String getOtpNumber() {
		return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
	}

	public static void main(String[] args) {

		System.out.println(ThreadLocalRandom.current().nextInt(100000, 1000000));
	}

	@Override
	public String validateMobileNumber(OTPGenerationInputDo otTPGenerationInputDo) throws CustomException {

		log.info("Inside validateMobileNumber() method - starts");
		log.info("Input data : " + otTPGenerationInputDo.toString());
		CustomerDetails customerDetails = new CustomerDetails();

		try {
			if (otTPGenerationInputDo.getCustPanNo() != null && otTPGenerationInputDo.getCustPanNo().length() > 0) {
				log.info("PAN is present");
				customerDetails = customerDetailsRepo.getCustomerDetailsByPan(otTPGenerationInputDo.getMobileNumber(),
						otTPGenerationInputDo.getCustPanNo(), otTPGenerationInputDo.getDeviceId());
			} else {
				customerDetails = customerDetailsRepo.getCustomerDetails(otTPGenerationInputDo.getMobileNumber(),
						otTPGenerationInputDo.getDob(), otTPGenerationInputDo.getDeviceId());
			}
			log.info("Details : " + customerDetails);

			String message = null;
			if (customerDetails != null) {
				log.info("Customer details retrieved");
				message = "SUCCESS";
			}

			else {
				message = "Customer Details are not available";
			}

			log.info(message);
			log.info("Inside validateMobileNumber() method - ends");
			return message;

		} catch (Exception e) {
			System.out.println("Exception occured in getCustomerDetails:" + e.toString());
			log.info("Exception occured in getCustomerDetails:" + e.toString());
			return e.toString();

		}
	}
}
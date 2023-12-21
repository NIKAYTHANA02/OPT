package com.abfl.service.impl;

import com.abfl.data_objects.*;
import com.abfl.entity.OtpEntity;
import com.abfl.exception_handling.CustomException;
import com.abfl.exception_handling.CustomExceptionSchema;
import com.abfl.repository.OtpEntityRepo;
import com.abfl.service.ConfigService;
import com.abfl.service.OTPValidationService;
import com.abfl.service.TrnLogService;
import com.abfl.utiliy.CommonUtils;

import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;


@Component
@Slf4j
public class OTPValidationServiceImpl implements OTPValidationService {


    @Autowired
    OtpEntityRepo otpEntityRepo;
    
    @Autowired
    TrnLogService trnLogService;
    
    @Autowired
    ConfigService configService;

    @Value("${otpExpireSec}")
    private Long otpExpirySec;


    @Override
    public ResponseEntity<?> validateOtP(OTPValidationInputDo oTPValidationInputDo) throws CustomException {
        try {
        	System.out.println("Starting OTP Validation ");
        	log.info("Starting OTP Validation ");
        	String strReqJsonBody = new JSONObject(oTPValidationInputDo).toString();
        	Long lng=trnLogService.saveApiRequest("OTP-Service/validateOTP", strReqJsonBody);
        	
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
            
            OtpEntity otpEntity = new OtpEntity();
           
            Optional<OtpEntity> optOtpEntity = otpEntityRepo.getLatestOtpForMobileNumber(oTPValidationInputDo.getMobileNumber());

            /*********************Check in DB if OTP is exist or not*************************/
            if (optOtpEntity.isPresent()) {
                otpEntity = optOtpEntity.get();
            } else {
            	trnLogService.saveApiErrorResponse(lng, "OTP not exist");
                throw CustomException.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("Otp Not exist")
                        .build();

            }

            java.sql.Timestamp otpCreatedtime= otpEntity.getCreatedOn();

            java.util.Date dbDate=new Date(otpCreatedtime.getTime());

            String otpFromDB = otpEntity.getOtp();

            String otpFromRequest = oTPValidationInputDo.getOtp();
            System.out.println("OTP from DB : " +otpFromDB );
            System.out.println("OTP from Request: " + otpFromRequest);
            log.info("OTP from DB : " +otpFromDB );
            log.info("OTP from Request: " + otpFromRequest);
            
            //if mobile number is 99999999999 then skip validation
            if(otpFromDB.equals(otpFromRequest) && (oTPValidationInputDo.getMobileNumber().equals("9999999999"))) {
            	  ResponseTemplate response = ResponseTemplate.builder()
                          .status("200")
                          .message("OTP successfully verified.Please proceed")
                          .data(Collections.singletonMap("otp", ""))
                          .build();
              	System.out.println("End of OTP Validation ");
              	log.info("End of OTP Validation Success");
              	trnLogService.saveApiResponse(lng, "OTP successfully verified.");
                  return  ResponseEntity.status(HttpStatus.OK).body(response);
            }
            
            
            
            /************************************** Campare OTP *********************************/
            if (otpFromDB.equals(otpFromRequest)) { //If both user and DB OTP are same
            	Date otpCreationDate=new Date( otpEntity.getCreatedOn().getTime());
                otpCreationDate.setTime(otpCreationDate.getTime() + 13000);
                Date dateNow=new Date();
                Long durration= CommonUtils.getTimeDifferenceinSec(otpCreationDate,dateNow);

                if(durration >= otpExpirySec)
                {
                	log.info("OTP Time Expired.Try Again");
                	trnLogService.saveApiErrorResponse(lng, "OTP Time Expired.Try Again");
                	throw CustomException.builder()
                            .httpStatus(HttpStatus.CREATED)
                            .message("OTP Time Expired.Try Again")
                            .build();
                }

                ResponseTemplate response = ResponseTemplate.builder()
                        .status("200")
                        .message("OTP successfully verified.Please proceed")
                        .data(Collections.singletonMap("otp", ""))
                        .build();
            	System.out.println("End of OTP Validation ");
            	log.info("End of OTP Validation Success");
            	trnLogService.saveApiResponse(lng, "OTP successfully verified.Please proceed");
                return  ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
            	trnLogService.saveApiErrorResponse(lng, "Incorrect OTP entered.Please enter the valid OTP");
                throw CustomException.builder()
                        .httpStatus(HttpStatus.CREATED)
                        .message("Incorrect OTP entered.Please enter the valid OTP")
                        .build();

            }


        } catch (Exception e) {
            System.out.println("Exception occured in generateOtP:" + e.toString());
            log.info("Exception occured in generateOtP:" + e.toString());
            throw CustomException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("server error.Please try again sometime")
                    .build();


        }


    }

    public static void main(String[] args) throws ParseException {


        Date startTime=new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").parse("2022-25-06 23:34:10");

        Date endTime=new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").parse("2022-27-06 23:34:10");

        //java.util.Date endTime=new Date();

        long duration = (endTime.getTime() - startTime.getTime());
        System.out.println(duration);
        long difference_In_Seconds
                = (duration
                / 1000);
        System.out.println(difference_In_Seconds);
    }

}

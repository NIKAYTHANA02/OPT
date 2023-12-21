package com.abfl.data_objects;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Convert;

import com.abfl.aes.AesEncryptor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OTPGenerationInputDo implements Serializable {

	private static final long serialVersionUID = 4048798961366546485L;

    @Convert(converter = AesEncryptor.class)
	@Schema(description = "Enter Mobile Number.", example = "9942012341", required = true)
	private String mobileNumber;
	
	@Schema(description = "N- New, R- Reset Mpin", example = "R", required = true)
	private String optTypeInd;
	
    @Convert(converter = AesEncryptor.class)
	@Schema(description = "Enter Date of Birth(yyyy-MM-dd", example = "1994-05-05", required = false)
	//private Date dob;
	private String dob;
	
	@Schema(description = "Enter Device Id", example = "049f58156e4be7da21", required = false)
	private String deviceId;
	
	@Schema(description = "Blocked Indicator", example = "Y or N", required = true)
	private String blockedInd;
	
	@Schema(description = "customer id", example = "1001", required = true)
	private Long custId;

	@Schema(description = "SMS Indicator", example = "R", required = true)
	private String smsInd;
	
	@Convert(converter = AesEncryptor.class)
	@Schema(description = "Enter PAN number", example = "DTUFD4674E", required = false)
	private String custPanNo;
	
	@Schema(description = "isExistingCustomer", required = false)
	private Boolean isExistingCustomer;
}
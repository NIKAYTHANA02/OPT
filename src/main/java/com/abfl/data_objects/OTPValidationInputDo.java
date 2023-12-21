package com.abfl.data_objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OTPValidationInputDo implements Serializable {
 
    private static final long serialVersionUID = 4048798961366546485L;
 

    @Schema(description = "Enter Mobile Number.",
            example = "9942012341", required = true)
    private String mobileNumber;


    @Schema(description = "Enter OTP.",
            example = "123456", required = true)
    private String otp;
}
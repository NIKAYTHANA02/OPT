package com.abfl.controller;

import com.abfl.data_objects.OTPGenerationInputDo;
import com.abfl.data_objects.OTPValidationInputDo;
import com.abfl.exception_handling.CustomException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface OTPValidation {


    /*   Get Loan Details        */

    @Operation(summary = "This is for Validating OTP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "OTP successfully verified.Please proceed",

                    content = @Content(
                     examples = @ExampleObject(
                              value = "{\n" +
                             "  \"status\": \"200\",\n" +
                             "  \"message\": \"OTP sent successfully to your registered mobile number.\",\n" +
                             "  \"object\": {\n" +
                             "    \"otp\": \"641035\"\n" +
                             "  }\n" +
                             "}"
                     )
                    )


            ),
            @ApiResponse(responseCode = "201",
                    description = "Incorrect OTP entered.Please enter the valid OTP",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "\t\"message\": \"Incorrect OTP entered.Please enter the valid OTP\",\n" +
                                            "\t\"status\": \"201\"\n" +
                                            "\t\n" +
                                            "}"
                            )
                    )

             ),
            @ApiResponse(responseCode = "201",
                    description = "OTP expired.Please enter the valid OTP",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "\t\"message\": \"OTP expired.Please enter the valid OTP\",\n" +
                                            "\t\"status\": \"201\"\n" +
                                            "}"
                            )
                    )
            ),

            @ApiResponse(responseCode = "400",
                    description = "server error.Please try again sometime",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "\t\"message\": \"server error.Please try again sometime\",\n" +
                                            "\t\"status\": \"400\"\n" +
                                            "}"
                            )
                    )

            )
    })
    @PostMapping(value = "/otpValidation")
    public ResponseEntity<?>  otpValidation(@RequestBody OTPValidationInputDo otpValidationInputDo) throws CustomException;

}

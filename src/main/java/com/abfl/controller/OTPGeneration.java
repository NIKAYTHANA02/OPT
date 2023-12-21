package com.abfl.controller;

import com.abfl.data_objects.*;
import com.abfl.exception_handling.CustomException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface OTPGeneration {


    /*   Get Loan Details        */

    @Operation(summary = "This is for Generating OTP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "OTP sent successfully to your registered mobile number.",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "\t\"message\": \"OTP sent successfully to your registered mobile number.\",\n" +
                                            "\t\"status\": \"200\",\n" +
                                            "\t\"data\": {\n" +
                                            "\t\t\"OTP\": \"641035\",\n" +
                                            "\t}\n" +
                                            "}"
                            )
                    )

            ),

            @ApiResponse(responseCode = "201",
                    description = "Mobile number doesn't exist.Please enter the valid mobile number",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "\t\"message\": \"Mobile number doesn't exist.Please enter the valid mobile number\",\n" +
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
                                            "\t\n" +
                                            "}"
                            )
                    )

            )
    })
    @PostMapping(value = "/otpGeneration")
    public ResponseEntity<?>  otpGeneration(@RequestBody OTPGenerationInputDo otpGenerationInputDo) throws CustomException;

}

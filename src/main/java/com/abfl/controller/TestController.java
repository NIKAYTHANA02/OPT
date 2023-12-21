package com.abfl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;

public interface TestController {

    @Operation(summary = "This is for summary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "return fine ok"


            ),
            @ApiResponse(responseCode = "404",
                    description = "return fine ok"

            ),
            @ApiResponse(responseCode = "501",
                    description = "return fine ok"

            )
    })
    @GetMapping(value = "/check")
    public String check();




}

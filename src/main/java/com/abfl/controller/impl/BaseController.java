package com.abfl.controller.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Testing Controller", description = "")
@RestController
@CrossOrigin("*")
public class BaseController {


    @GetMapping(value = "/")
    public ResponseEntity<?> baseMethod()
    {
        return  ResponseEntity.status(HttpStatus.OK).body(null);
    }


}

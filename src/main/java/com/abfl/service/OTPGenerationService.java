package com.abfl.service;

import com.abfl.data_objects.CustomerDetails;
import com.abfl.data_objects.OTPGenerationInputDo;
import com.abfl.data_objects.OTPGenerationOutDo;
import com.abfl.exception_handling.CustomException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface OTPGenerationService {

    public ResponseEntity<?> generateOtP(OTPGenerationInputDo otTPGenerationInputDo) throws CustomException;
    
    
    public String validateMobileNumber(OTPGenerationInputDo otTPGenerationInputDo) throws CustomException;

}

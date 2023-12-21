package com.abfl.data_objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OTPGClientResponseDo implements Serializable {
 
    private static final long serialVersionUID = 4048798961366546485L;
 
    private String otp;
}
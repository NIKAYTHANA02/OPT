package com.abfl.data_objects;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OTPGenerationOutDo implements Serializable {

	private static final long serialVersionUID = 4048798961366546485L;

	private String otp;
}
package com.abfl.data_objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmsApiResponse {

	@JsonProperty("status")
	public String status;

	@JsonProperty(value = "data", required = false)
	public Object data;

	@JsonProperty("message")
	public String message;

	public void print() {
		log.info(this.toString());
	}

}
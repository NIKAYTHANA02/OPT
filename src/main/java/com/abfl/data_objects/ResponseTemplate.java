package com.abfl.data_objects;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResponseTemplate {
    String status;

    String message;

    Object data;


    public static ResponseEntity<?> okResponse( String message,Object obj) {
        ResponseTemplate response= ResponseTemplate.builder()
                .status("200")
                .message(message)
                .data(obj)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

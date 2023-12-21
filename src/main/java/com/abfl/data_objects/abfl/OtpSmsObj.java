package com.abfl.data_objects.abfl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpSmsObj {

    @JsonProperty("message")
    public String message;
    @JsonProperty("sender")
    public String sender;
    @JsonProperty("sms")
    public Sms sms;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "to",
            "msgid"
    })
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Sms {

        @JsonProperty("to")
        public String to;
        @JsonProperty("msgid")
        public String msgid;

    }
}

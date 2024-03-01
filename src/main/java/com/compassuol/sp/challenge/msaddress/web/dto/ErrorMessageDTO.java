package com.compassuol.sp.challenge.msaddress.web.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorMessageDTO {
    private final int code;
    private final String status;
    private final String message;

    public ErrorMessageDTO(HttpStatus status, String message) {
        this.code = status.value();
        this.status = status.getReasonPhrase();
        this.message = message;
    }
}
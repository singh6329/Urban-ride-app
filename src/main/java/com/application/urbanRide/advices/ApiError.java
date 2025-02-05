package com.application.urbanRide.advices;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiError {
    private LocalDateTime timeStamp;
    private HttpStatus httpStatus;
    private String error;

    public ApiError() {
        this.timeStamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus httpStatus, String error) {
        this();
        this.httpStatus = httpStatus;
        this.error = error;
    }
}

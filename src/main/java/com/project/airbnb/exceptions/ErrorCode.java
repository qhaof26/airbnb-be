package com.project.airbnb.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    //USER
    USER_NOT_EXISTED(HttpStatus.NOT_FOUND, "User not found !"),
    EMAIL_EXISTED(HttpStatus.BAD_REQUEST, "Email existed !"),
    USERNAME_EXISTED(HttpStatus.BAD_REQUEST, "User existed !")
    ;

    private final HttpStatusCode statusCode;
    private final String message;

    ErrorCode(HttpStatusCode statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}

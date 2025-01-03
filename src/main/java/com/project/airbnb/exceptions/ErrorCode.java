package com.project.airbnb.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    //ROLE
    ROLE_EXISTED(HttpStatus.BAD_REQUEST, "Role existed !"),
    ROLE_NOT_EXISTED(HttpStatus.NOT_FOUND, "Role not existed !"),
    //USER
    USER_NOT_EXISTED(HttpStatus.NOT_FOUND, "User not found !"),
    EMAIL_EXISTED(HttpStatus.BAD_REQUEST, "Email existed !"),
    USERNAME_EXISTED(HttpStatus.BAD_REQUEST, "Username existed !")
    ;

    private final HttpStatusCode statusCode;
    private final String message;

    ErrorCode(HttpStatusCode statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}

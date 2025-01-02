package com.project.airbnb.exceptions;

import com.project.airbnb.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ErrorResponse handlingRuntimeException(RuntimeException exception){
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();
    }

    @ExceptionHandler(value = AppException.class)
    public ErrorResponse handlingAppException(AppException exception){
        ErrorCode errorCode = exception.getErrorCode();
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .statusCode(errorCode.getStatusCode())
                .message(errorCode.getMessage())
                .build();
    }

}

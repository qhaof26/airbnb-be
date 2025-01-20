package com.project.airbnb.exceptions;

import com.project.airbnb.dtos.response.APIResponse;
import com.project.airbnb.dtos.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import org.springframework.validation.FieldError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({Exception.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handlingRuntimeException(RuntimeException exception){

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponse.builder()
                        .timestamp(Instant.now())
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(exception.getLocalizedMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ErrorResponse> handlingAppException(AppException exception){
        ErrorCode errorCode = exception.getErrorCode();

        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ErrorResponse.builder()
                        .timestamp(Instant.now())
                        .statusCode(errorCode.getStatusCode().value())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse<?>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String enumKey = null;
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;

        FieldError fieldError = exception.getFieldError();
        if (fieldError != null) {
            enumKey = fieldError.getDefaultMessage();
        }

        try {
            if (enumKey != null) {
                errorCode = ErrorCode.valueOf(enumKey);
            }

            var bindingResult = exception.getBindingResult();
            var errorObject = bindingResult.getAllErrors().get(0);
            if (errorObject instanceof ConstraintViolation) {
                var constraintViolation = (ConstraintViolation<?>) errorObject;
                attributes = constraintViolation.getConstraintDescriptor().getAttributes();
                log.info(attributes.toString());
            }
        } catch (IllegalArgumentException e) {
            log.warn("Invalid error code: " + enumKey, e);
        }

        String message = errorCode.getMessage(); // Lấy message từ ErrorCode hoặc từ attributes
        return ResponseEntity.badRequest().body(
                APIResponse.builder()
                        .status(errorCode.getStatusCode().value())
                        .message(message)
                        .build()
        );
    }


}

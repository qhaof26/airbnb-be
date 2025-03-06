package com.project.airbnb.exception;

public class FileUploadException extends AppException{
    public FileUploadException(ErrorCode errorCode) {
        super(errorCode);
    }
}

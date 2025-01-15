package com.project.airbnb.exceptions;

public class FileUploadException extends AppException{
    public FileUploadException(ErrorCode errorCode) {
        super(errorCode);
    }
}

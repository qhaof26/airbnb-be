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
    USER_NOT_EXISTED(HttpStatus.NOT_FOUND, "User not existed !"),
    EMAIL_EXISTED(HttpStatus.BAD_REQUEST, "Email existed !"),
    USERNAME_EXISTED(HttpStatus.BAD_REQUEST, "Username existed !"),
    //LOCATION
    PROVINCE_NOT_EXISTED(HttpStatus.NOT_FOUND, "Province not existed !"),
    DISTRICT_NOT_EXISTED(HttpStatus.NOT_FOUND, "District not existed !"),
    WARD_NOT_EXISTED(HttpStatus.NOT_FOUND, "Ward not existed !"),
    //CATEGORY
    CATEGORY_NOT_EXISTED(HttpStatus.NOT_FOUND, "Category not existed !"),
    CATEGORY_EXISTED(HttpStatus.BAD_REQUEST, "Category existed !"),
    //AMENITY
    AMENITY_NOT_EXISTED(HttpStatus.NOT_FOUND, "Amenity not existed !"),
    AMENITY_EXISTED(HttpStatus.BAD_REQUEST, "Amenity existed !"),
    //LISTING
    LISTING_NOT_EXISTED(HttpStatus.NOT_FOUND, "Listing not existed !"),
    LISTING_EXISTED(HttpStatus.BAD_REQUEST, "Listing existed !")
    ;

    private final HttpStatusCode statusCode;
    private final String message;

    ErrorCode(HttpStatusCode statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}

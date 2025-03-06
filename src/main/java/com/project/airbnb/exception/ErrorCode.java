package com.project.airbnb.exception;

import com.project.airbnb.constant.AppConst;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    INVALID_KEY(HttpStatus.BAD_REQUEST, "Uncategorized error"),
    //FILE
    FILE_ERROR(HttpStatus.BAD_REQUEST, "Could not read file "),

    //AUTH
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED,"Unauthenticated"),
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "You do not have permission"),
    //ROLE
    ROLE_EXISTED(HttpStatus.BAD_REQUEST, "Role existed !"),
    ROLE_NOT_EXISTED(HttpStatus.NOT_FOUND, "Role not existed !"),
    //USER
    USER_VERIFIED(HttpStatus.BAD_REQUEST, "User is already verified !"),
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
    LISTING_EXISTED(HttpStatus.BAD_REQUEST, "Listing existed !"),
    LISTING_NOT_EMPTY(HttpStatus.BAD_REQUEST, "Listing not empty !"),
    GUEST_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "The number of guests has exceeded the prescribed limit"),
    LISTING_IMAGE_MAX_QUANTITY(HttpStatus.BAD_REQUEST, "Number of images listing must be <= " + AppConst.MAXIMUM_IMAGE_PER_LISTING),
    IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "Image not found"),

    //LISTING_AVAILABILITY
    LISTING_AVAILABILITY_NOT_EXISTED(HttpStatus.NOT_FOUND, "Listing availability not existed !"),
    LISTING_AVAILABILITY_EXISTED(HttpStatus.BAD_REQUEST, "Listing availability existed !"),
    //BOOKING
    BOOKING_NOT_EXISTED(HttpStatus.NOT_FOUND, "Booking not existed !"),
    BOOKING_EXISTED(HttpStatus.BAD_REQUEST, "Booking existed !"),
    //REVIEW
    REVIEW_NOT_EXISTED(HttpStatus.BAD_REQUEST, "Review not existed !"),
    REVIEW_MAX_STAR(HttpStatus.BAD_REQUEST, "Star of review listing must be <= " + AppConst.MAXIMUM_STAR),
    //NOTIFICATION
    NOTIFICATION_NOT_EXISTED(HttpStatus.NOT_FOUND, "Notification not existed !");
    ;

    private final HttpStatusCode statusCode;
    private final String message;

    ErrorCode(HttpStatusCode statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}

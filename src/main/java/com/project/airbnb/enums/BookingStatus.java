package com.project.airbnb.enums;

public enum BookingStatus {
    PENDING,
    CONFIRMED,
    CHECKED_IN,
    CHECKED_OUT,
    EXPIRED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    FAILED,
    NO_SHOW,
    REFUNDED, //Người dùng hủy đặt phòng trong thời gian quy định và đủ điều kiện nhận hoàn tiền.

    // GUEST + Booking -> status: PENDING
    // HOST + checked -> status(PENDING): CONFIRMED
    // GUEST + check in -> status(CONFIRMED): CHECKED_IN
    // Scheduling date now = date check out -> status(CHECKED_IN): CHECKED_OUT
    // Scheduling HOST not confirm -> status(PENDING): EXPIRED
    // GUEST + payment + review -> status(CHECKED_OUT): COMPLETED

    // GUEST + cancel booking + reasons -> status(PENDING || CONFIRMED): CANCELLED
    //      if(today < check_in 3 day) -> status(PENDING || CONFIRMED): CANCELLED + refund money 100%
    //      if(check_in < today < checkout) -> status(CHECKED_IN): CANCELLED + refund money * (days)
    // GUEST + no show in date check_in -> status(CONFIRMED): NO_SHOW
    //      if(today > check_in 1 day) status(NO_SHOW) -> CANCELLED
}

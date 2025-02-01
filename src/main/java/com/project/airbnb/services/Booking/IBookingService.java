package com.project.airbnb.services.Booking;

import com.project.airbnb.dtos.request.BookingCreationRequest;
import com.project.airbnb.dtos.request.BookingUpdateRequest;
import com.project.airbnb.dtos.response.BookingResponse;
import com.project.airbnb.dtos.response.PageResponse;

import java.util.List;

public interface IBookingService {
    BookingResponse getBookingById(String bookingId);

    PageResponse<List<BookingResponse>> getBookingsOfGuest(int pageNo, int pageSize); // GUEST

    PageResponse<List<BookingResponse>> getBookingsOfHost(int pageNo, int pageSize); // HOST

    BookingResponse createBooking(BookingCreationRequest request); // GUEST

    BookingResponse updateBooking(BookingUpdateRequest request); // HOST
}

package com.project.airbnb.service.Booking;

import com.project.airbnb.dto.request.BookingCreationRequest;
import com.project.airbnb.dto.request.BookingUpdateRequest;
import com.project.airbnb.dto.response.BookingResponse;
import com.project.airbnb.dto.response.PageResponse;

import java.util.List;

public interface IBookingService {
    BookingResponse getBookingById(String bookingId);

    PageResponse<List<BookingResponse>> getBookingsOfGuest(int pageNo, int pageSize); // GUEST

    PageResponse<List<BookingResponse>> getBookingsOfHost(int pageNo, int pageSize); // HOST

    BookingResponse createBooking(BookingCreationRequest request); // GUEST

    BookingResponse updateBooking(BookingUpdateRequest request); // HOST
}

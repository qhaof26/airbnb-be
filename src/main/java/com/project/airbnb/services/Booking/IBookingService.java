package com.project.airbnb.services.Booking;

import com.project.airbnb.dtos.request.BookingCreationRequest;
import com.project.airbnb.dtos.request.BookingUpdateRequest;
import com.project.airbnb.dtos.response.BookingResponse;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.models.User;

import java.util.List;

public interface IBookingService {
    BookingResponse getBookingById(String bookingId);
    PageResponse<List<BookingResponse>> getAllBookingByGuest(User user, int pageNo, int pageSize); //GUEST
    PageResponse<List<BookingResponse>> getAllBookingByHost(String userId, int pageNo, int pageSize); //HOST
    BookingResponse createBooking(BookingCreationRequest request); //GUEST
    BookingResponse updateBooking(BookingUpdateRequest request); //HOST
}

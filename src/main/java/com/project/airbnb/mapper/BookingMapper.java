package com.project.airbnb.mapper;

import com.project.airbnb.dto.response.BookingResponse;
import com.project.airbnb.model.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {
    public BookingResponse toBookingResponse(Booking booking) {
        BookingResponse.Listing listing = new BookingResponse.Listing();
        listing.setId(booking.getListing().getId());
        listing.setListingName(booking.getListing().getListingName());
        return BookingResponse.builder()
                .id(booking.getId())
                .checkinDate(booking.getCheckinDate())
                .checkoutDate(booking.getCheckoutDate())
                .listing(listing)
                .numGuests(booking.getNumGuests())
                .totalAmount(booking.getTotalAmount())
                .currency(booking.getCurrency())
                .note(booking.getNote())
                .status(booking.getStatus())
                .build();
    }
}

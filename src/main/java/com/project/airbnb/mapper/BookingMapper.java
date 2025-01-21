package com.project.airbnb.mapper;

import com.project.airbnb.dtos.response.BookingResponse;
import com.project.airbnb.models.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {
    public BookingResponse toBookingResponse(Booking booking){
        BookingResponse.Listing listing = new BookingResponse.Listing();
        listing.setId(booking.getListing().getId());
        listing.setListingName(booking.getListing().getListingName());

        return BookingResponse.builder()
                .id(booking.getId())
                .checkinDate(booking.getCheckinDate())
                .checkoutDate(booking.getCheckoutDate())
                .listing(listing)
//                .nightlyPrice(booking.getNightlyPrice())
//                .serviceFee(booking.getServiceFee())
//                .totalPrice(booking.getTotalPrice())
                .numGuests(booking.getNumGuests())
                .note(booking.getNote())
                .status(booking.getStatus())
                .build();
    }
}

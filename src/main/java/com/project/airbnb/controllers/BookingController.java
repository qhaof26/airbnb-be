package com.project.airbnb.controllers;

import com.project.airbnb.dtos.request.BookingCreationRequest;
import com.project.airbnb.dtos.request.BookingUpdateRequest;
import com.project.airbnb.dtos.response.APIResponse;
import com.project.airbnb.dtos.response.BookingResponse;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.models.User;
import com.project.airbnb.services.Booking.BookingService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public APIResponse<BookingResponse> getBookingById(@PathVariable String bookingId){
        return APIResponse.<BookingResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get booking by id")
                .data(bookingService.getBookingById(bookingId))
                .build();
    }

    @GetMapping("/guest")
    public APIResponse<PageResponse<List<BookingResponse>>> getAllBookingsOfGuest(
            @RequestBody User user,
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ){
        return APIResponse.<PageResponse<List<BookingResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Get all booking of Guest")
                .data(bookingService.getAllBookingByGuest(user, pageNo, pageSize))
                .build();
    }

    @GetMapping("/host/{userId}")
    public APIResponse<PageResponse<List<BookingResponse>>> getAllBookingOfHost(
            @PathVariable String userId,
            @Min(value = 1)@RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ){
        return APIResponse.<PageResponse<List<BookingResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Get all booking of host")
                .data(bookingService.getAllBookingByHost(userId, pageNo, pageSize))
                .build();
    }

    @PostMapping("/create")
    public APIResponse<BookingResponse> createBooking(@RequestBody BookingCreationRequest request){
        return APIResponse.<BookingResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created booking")
                .data(bookingService.createBooking(request))
                .build();
    }

    @PatchMapping("/update")
    public APIResponse<BookingResponse> updateListing(@RequestBody BookingUpdateRequest request){
        return APIResponse.<BookingResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Update status booking")
                .data(bookingService.updateBooking(request))
                .build();
    }
}

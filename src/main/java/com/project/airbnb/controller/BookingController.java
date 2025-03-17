package com.project.airbnb.controller;

import com.project.airbnb.dto.request.BookingCreationRequest;
import com.project.airbnb.dto.request.BookingUpdateRequest;
import com.project.airbnb.dto.response.APIResponse;
import com.project.airbnb.dto.response.BookingResponse;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.service.Booking.BookingService;
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
        public APIResponse<BookingResponse> getBookingById(@PathVariable String bookingId) {
                return APIResponse.<BookingResponse>builder()
                                .status(HttpStatus.OK.value())
                                .message("Get booking by id")
                                .data(bookingService.getBookingById(bookingId))
                                .build();
        }

        @GetMapping("/guest")
        public APIResponse<PageResponse<List<BookingResponse>>> getAllBookingsOfGuest(
                        @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
                        @RequestParam(defaultValue = "10", required = false) int pageSize) {
                return APIResponse.<PageResponse<List<BookingResponse>>>builder()
                                .status(HttpStatus.OK.value())
                                .message("Get all booking of Guest")
                                .data(bookingService.getBookingsOfGuest(pageNo, pageSize))
                                .build();
        }

        @GetMapping("/host")
        public APIResponse<PageResponse<List<BookingResponse>>> getAllBookingOfHost(
                        @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
                        @RequestParam(defaultValue = "10", required = false) int pageSize) {
                return APIResponse.<PageResponse<List<BookingResponse>>>builder()
                                .status(HttpStatus.OK.value())
                                .message("Get all booking of host")
                                .data(bookingService.getBookingsOfHost(pageNo, pageSize))
                                .build();
        }

        @PostMapping
        public APIResponse<BookingResponse> createBooking(@RequestBody BookingCreationRequest request) {
                return APIResponse.<BookingResponse>builder()
                                .status(HttpStatus.CREATED.value())
                                .message("Created booking")
                                .data(bookingService.createBooking(request))
                                .build();
        }

        @PatchMapping
        public APIResponse<BookingResponse> updateListing(@RequestBody BookingUpdateRequest request) {
                return APIResponse.<BookingResponse>builder()
                                .status(HttpStatus.OK.value())
                                .message("Update status booking")
                                .data(bookingService.updateBooking(request))
                                .build();
        }
}

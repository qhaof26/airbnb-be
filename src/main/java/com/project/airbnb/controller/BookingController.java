package com.project.airbnb.controller;

import com.project.airbnb.dto.request.BookingCreationRequest;
import com.project.airbnb.dto.request.BookingUpdateRequest;
import com.project.airbnb.dto.response.APIResponse;
import com.project.airbnb.dto.response.BookingResponse;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.service.Booking.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/bookings")
@Tag(name = "Booking")
public class BookingController {
        private final BookingService bookingService;

        @Operation(summary = "Get booking detail", description = "Send a request via this API to get booking information")
        @GetMapping("/{bookingId}")
        public APIResponse<BookingResponse> getBookingById(@PathVariable String bookingId) {
                return APIResponse.<BookingResponse>builder()
                                .status(HttpStatus.OK.value())
                                .message("Get booking by id")
                                .data(bookingService.getBookingById(bookingId))
                                .build();
        }

        @Operation(summary = "Get booking of the guest", description = "Send a request via this API to get booking of the guest")
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

        @Operation(summary = "Get booking of the host", description = "Send a request via this API to get booking of the host")
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

        @Operation(summary = "Add new booking", description = "Send a request via this API to add new booking")
        @PostMapping
        public APIResponse<BookingResponse> createBooking(@RequestBody BookingCreationRequest request) {
                return APIResponse.<BookingResponse>builder()
                                .status(HttpStatus.CREATED.value())
                                .message("Created booking")
                                .data(bookingService.createBooking(request))
                                .build();
        }

        @Operation(summary = "Update status of booking", description = "Send a request via this API to update status of booking")
        @PatchMapping
        public APIResponse<BookingResponse> updateListing(@RequestBody BookingUpdateRequest request) {
                return APIResponse.<BookingResponse>builder()
                                .status(HttpStatus.OK.value())
                                .message("Update status booking")
                                .data(bookingService.updateBooking(request))
                                .build();
        }
}

package com.project.airbnb.service.Booking;

import com.project.airbnb.constant.AppConst;
import com.project.airbnb.dto.request.BookingCreationRequest;
import com.project.airbnb.dto.request.BookingUpdateRequest;
import com.project.airbnb.dto.response.BookingResponse;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.enums.BookingStatus;
import com.project.airbnb.enums.Currency;
import com.project.airbnb.enums.ListingAvailabilityStatus;
import com.project.airbnb.exception.AppException;
import com.project.airbnb.exception.ErrorCode;
import com.project.airbnb.mapper.BookingMapper;
import com.project.airbnb.model.*;
import com.project.airbnb.repository.BookingRepository;
import com.project.airbnb.repository.ListingAvailabilityRepository;
import com.project.airbnb.repository.ListingRepository;
import com.project.airbnb.repository.UserRepository;
import com.project.airbnb.service.Email.EmailService;
import com.project.airbnb.service.Notification.NotificationService;
import com.project.airbnb.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService{
    private final BookingRepository bookingRepository;
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final ListingAvailabilityRepository availabilityRepository;
    private final EmailService emailService;
    private final NotificationService notificationService;
    private final BookingMapper bookingMapper;

    @Override
    public BookingResponse getBookingById(String bookingId) {
        return bookingMapper.toBookingResponse(bookingRepository.findById(bookingId).orElseThrow(()->new AppException(ErrorCode.BOOKING_NOT_EXISTED)));
    }

    @Override
    @PreAuthorize("hasRole('GUEST') or hasRole('ADMIN')")
    public PageResponse<List<BookingResponse>> getBookingsOfGuest(int pageNo, int pageSize) {
        String username = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        Page<Booking> bookingPage = bookingRepository.findAllByUser(user, pageable);
        List<BookingResponse> bookingResponses = bookingPage.getContent().stream().map(bookingMapper::toBookingResponse).toList();

        return PageResponse.<List<BookingResponse>>builder()
                .page(pageable.getPageNumber()+1)
                .size(pageable.getPageSize())
                .totalElement(bookingPage.getTotalElements())
                .totalPage(bookingPage.getTotalPages())
                .data(bookingResponses)
                .build();
    }

    @Override
    @PreAuthorize("hasRole('HOST') or hasRole('ADMIN')")
    public PageResponse<List<BookingResponse>> getBookingsOfHost(int pageNo, int pageSize) {
        String username = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        Page<Booking> bookingPage = bookingRepository.findAllByHost(user.getId(), pageable);
        List<BookingResponse> bookingResponses = bookingPage.getContent().stream().map(bookingMapper::toBookingResponse).toList();

        return PageResponse.<List<BookingResponse>>builder()
                .page(pageable.getPageNumber()+1)
                .size(pageable.getPageSize())
                .totalElement(bookingPage.getTotalElements())
                .totalPage(bookingPage.getTotalPages())
                .data(bookingResponses)
                .build();
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public BookingResponse createBooking(BookingCreationRequest request) {
        Listing listing = listingRepository.findById(request.getListing().getId()).orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_EXISTED));
        if(!availabilityRepository.existsByListing(request.getListing())) throw new AppException(ErrorCode.LISTING_AVAILABILITY_NOT_EXISTED);
        if(!request.getCheckinDate().isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Check-in date must be after today");
        }
        if(!request.getCheckoutDate().isAfter(request.getCheckinDate())){
            throw new IllegalArgumentException("Check-out date must be after Check-in date");
        }
        List<ListingAvailability> listingAvailabilities = availabilityRepository.checkListingAndDateRangeAndStatus(request.getListing().getId(),request.getCheckinDate(), request.getCheckoutDate(), ListingAvailabilityStatus.AVAILABLE);

        final long totalDays = ChronoUnit.DAYS.between(request.getCheckinDate(), request.getCheckoutDate()) + 1;
        if(listingAvailabilities.size() != totalDays){
            throw new AppException(ErrorCode.LISTING_NOT_EMPTY);
        }
        if(request.getNumGuests() > listing.getNumGuests()){
            throw new AppException(ErrorCode.GUEST_LIMIT_EXCEEDED);
        }

        BigDecimal totalAmount = calculate(listingAvailabilities);

        Booking booking = Booking.builder()
                .checkinDate(request.getCheckinDate())
                .checkoutDate(request.getCheckoutDate())
                .numGuests(request.getNumGuests())
                .totalAmount(totalAmount)
                .currency(Currency.VND.getValue())
                .note(request.getNote())
                .status(BookingStatus.DRAFT)
                .listing(listing)
                .user(getUserLogin())
                .build();
        bookingRepository.save(booking);
        listingAvailabilities.forEach(listingAvailability -> listingAvailability.setStatus(ListingAvailabilityStatus.HELD));
        availabilityRepository.saveAll(listingAvailabilities);

        User host = listing.getHost();
        sendVerificationEmail(host.getEmail(), listing, booking);
        sendVerificationEmail(getUserLogin().getEmail(), listing, booking);

        notificationService.sendNotification(host, "New Booking !", "Guest: " + getUserLogin().getId() + " has booked " + listing.getListingName());

        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    @PreAuthorize("hasRole('HOST')")
    @Transactional
    public BookingResponse updateBooking(BookingUpdateRequest request) {
        Booking booking = bookingRepository.findById(request.getId()).orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXISTED));
        if(!getUserLogin().equals(booking.getListing().getHost())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        booking.setStatus(request.getStatus());
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        notificationService.sendNotification(booking.getUser(), "Change status booking !", "The host has updated the status of the booking: " + request.getStatus());

        return bookingMapper.toBookingResponse(booking);
    }

    private User getUserLogin(){
        String username = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        return userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    private void sendVerificationEmail(String email, Listing listing, Booking booking){
        String subject = AppConst.TITTLE_BOOKING;
        String body = "Id booking : " + booking.getId() + " - Listing Name: " + listing.getListingName();
        emailService.sendOtpRegister(email, subject, body);
    }

    private BigDecimal calculate(final List<ListingAvailability> availabilities){
        var subtotal = BigDecimal.ZERO;
        for(var aDay : availabilities){
            subtotal = subtotal.add(aDay.getPrice());
        }
        return subtotal;
    }
}

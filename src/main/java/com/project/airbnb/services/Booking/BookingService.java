package com.project.airbnb.services.Booking;

import com.project.airbnb.constants.AppConst;
import com.project.airbnb.dtos.request.BookingCreationRequest;
import com.project.airbnb.dtos.request.BookingUpdateRequest;
import com.project.airbnb.dtos.response.BookingResponse;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.enums.BookingStatus;
import com.project.airbnb.enums.Currency;
import com.project.airbnb.enums.ListingAvailabilityStatus;
import com.project.airbnb.exceptions.AppException;
import com.project.airbnb.exceptions.ErrorCode;
import com.project.airbnb.mapper.BookingMapper;
import com.project.airbnb.models.Booking;
import com.project.airbnb.models.Listing;
import com.project.airbnb.models.ListingAvailability;
import com.project.airbnb.models.User;
import com.project.airbnb.repositories.BookingRepository;
import com.project.airbnb.repositories.ListingAvailabilityRepository;
import com.project.airbnb.repositories.ListingRepository;
import com.project.airbnb.repositories.UserRepository;
import com.project.airbnb.services.Email.EmailService;
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
    //CREATE Role: GUEST
    //Check ListingAvailability
    //Check num guests <= num guest of Listing
    //Calculate total price (checkinDate -> checkoutDate). Check price in ListingAvailability

    //GET Role: HOST
    //Check Listing in Booking -> Check User in Listing -> HOST: get all booking of listing
    private final BookingRepository bookingRepository;
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final ListingAvailabilityRepository availabilityRepository;
    private final EmailService emailService;
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
    @PreAuthorize("hasRole('GUEST') or hasRole('HOST') or hasRole('ADMIN')")
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

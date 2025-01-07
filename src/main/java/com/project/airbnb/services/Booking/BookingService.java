package com.project.airbnb.services.Booking;

import com.project.airbnb.dtos.request.BookingCreationRequest;
import com.project.airbnb.dtos.request.BookingUpdateRequest;
import com.project.airbnb.dtos.response.BookingResponse;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.enums.BookingStatus;
import com.project.airbnb.enums.ListingStatus;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private final BookingMapper bookingMapper;

    @Override
    public BookingResponse getBookingById(String bookingId) {
        return bookingMapper.toBookingResponse(bookingRepository.findById(bookingId).orElseThrow(()->new AppException(ErrorCode.BOOKING_NOT_EXISTED)));
    }

    @Override
    public PageResponse<List<BookingResponse>> getAllBookingByGuest(User user, int pageNo, int pageSize) {
        if(!userRepository.existsById(user.getId())){
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
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
    public PageResponse<List<BookingResponse>> getAllBookingByHost(String userId, int pageNo, int pageSize) {
        if(!userRepository.existsById(userId)){
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        Page<Booking> bookingPage = bookingRepository.findAllByHost(userId, pageable);
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
    @Transactional
    public BookingResponse createBooking(BookingCreationRequest request) {
        Listing listing = listingRepository.findById(request.getListing().getId()).orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_EXISTED));
        if(!availabilityRepository.existsByListing(request.getListing())) throw new AppException(ErrorCode.LISTING_AVAILABILITY_NOT_EXISTED);
        if(!request.getCheckoutDate().isAfter(request.getCheckinDate())){
            throw new IllegalArgumentException("Check-out date must be after Check-in date");
        }
        List<ListingAvailability> listingAvailabilities = availabilityRepository.checkListingAndDateRangeAndStatus(request.getListing().getId(),request.getCheckinDate(), request.getCheckoutDate(), ListingStatus.AVAILABLE);

        long totalDays = ChronoUnit.DAYS.between(request.getCheckinDate(), request.getCheckoutDate()) + 1;
        if(listingAvailabilities.size() != totalDays){
            throw new AppException(ErrorCode.LISTING_NOT_EMPTY);
        }
        if(request.getNumGuests() > listing.getNumGuests()){
            throw new AppException(ErrorCode.GUEST_LIMIT_EXCEEDED);
        }

        long numDays = ChronoUnit.DAYS.between(request.getCheckinDate(), request.getCheckoutDate());
        BigDecimal serviceFee = BigDecimal.valueOf(100000);
        BigDecimal totalPrice = (listing.getNightlyPrice()).multiply(BigDecimal.valueOf(numDays));

        //User is logging (JWT)
        User userBooking = userRepository.findByEmail("admin@gmail.com").orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));

        Booking booking = Booking.builder()
                .checkinDate(request.getCheckinDate())
                .checkoutDate(request.getCheckoutDate())
                .nightlyPrice(listing.getNightlyPrice())
                .serviceFee(BigDecimal.valueOf(100000))
                .totalPrice(totalPrice.add(serviceFee))
                .numGuests(request.getNumGuests())
                .note(request.getNote())
                .status(BookingStatus.PENDING)
                .listing(listing)
                .user(userBooking)
                .build();
        bookingRepository.save(booking);

        listingAvailabilities.forEach(listingAvailability -> listingAvailability.setStatus(ListingStatus.BOOKED));
        availabilityRepository.saveAll(listingAvailabilities);
        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse updateBooking(BookingUpdateRequest request) {
        Booking booking = bookingRepository.findById(request.getId()).orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXISTED));
        booking.setStatus(request.getStatus());
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);
        return bookingMapper.toBookingResponse(booking);
    }

}

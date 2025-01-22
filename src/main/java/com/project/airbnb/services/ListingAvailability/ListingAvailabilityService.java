package com.project.airbnb.services.ListingAvailability;

import com.project.airbnb.dtos.request.ListingAvailabilityCreationRequest;
import com.project.airbnb.dtos.request.ListingAvailabilityUpdateRequest;
import com.project.airbnb.dtos.response.ListingAvailabilityResponse;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.enums.ListingAvailabilityStatus;
import com.project.airbnb.exceptions.AppException;
import com.project.airbnb.exceptions.ErrorCode;
import com.project.airbnb.mapper.ListingAvailabilityMapper;
import com.project.airbnb.models.Listing;
import com.project.airbnb.models.ListingAvailability;
import com.project.airbnb.models.User;
import com.project.airbnb.repositories.ListingAvailabilityRepository;
import com.project.airbnb.repositories.ListingRepository;
import com.project.airbnb.repositories.UserRepository;
import com.project.airbnb.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListingAvailabilityService implements IListingAvailabilityService{
    private final ListingAvailabilityRepository listingAvailabilityRepository;
    private final ListingRepository listingRepository;
    private final ListingAvailabilityMapper listingAvailabilityMapper;
    private final UserRepository userRepository;

    @Override
    public ListingAvailabilityResponse getListingAvailability(String id) {
        return listingAvailabilityMapper.toListingAvailabilityResponse(listingAvailabilityRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LISTING_AVAILABILITY_NOT_EXISTED)));
    }

    @Override
    public PageResponse<List<ListingAvailabilityResponse>> fetchAvailabilityByListing(String listingId, int pageNo, int pageSize) {
        verifyHostOfListing(listingId);
        Listing listing = listingRepository.findById(listingId).orElseThrow(()->new AppException(ErrorCode.LISTING_NOT_EXISTED));
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        Page<ListingAvailability> listingAvailabilityPage = listingAvailabilityRepository.findByListing(listing, pageable);
        List<ListingAvailabilityResponse> availabilityResponses = listingAvailabilityPage.getContent().stream().map(listingAvailabilityMapper::toListingAvailabilityResponse).toList();

        return PageResponse.<List<ListingAvailabilityResponse>>builder()
                .page(pageable.getPageNumber()+1)
                .size(pageable.getPageSize())
                .totalPage(listingAvailabilityPage.getTotalPages())
                .totalElement(listingAvailabilityPage.getTotalElements())
                .data(availabilityResponses)
                .build();
    }

    @Override
    public PageResponse<List<ListingAvailabilityResponse>> fetchAvailabilityByTime(String listingId, LocalDate startDate, LocalDate endDate, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        Page<ListingAvailability> listingAvailabilityPage = listingAvailabilityRepository.findByListingAndDateRange(listingId, startDate, endDate, pageable);
        List<ListingAvailabilityResponse> availabilityResponses = listingAvailabilityPage.getContent().stream().map(listingAvailabilityMapper::toListingAvailabilityResponse).toList();

        return PageResponse.<List<ListingAvailabilityResponse>>builder()
                .page(pageable.getPageNumber()+1)
                .size(pageable.getPageSize())
                .totalPage(listingAvailabilityPage.getTotalPages())
                .totalElement(listingAvailabilityPage.getTotalElements())
                .data(availabilityResponses)
                .build();
    }

    @Override
    @PreAuthorize("hasRole('HOST') or hasRole('ADMIN')")
    @Transactional
    public ListingAvailabilityResponse createListingAvailability(ListingAvailabilityCreationRequest request) {
        String username = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Listing listing = listingRepository.findById(request.getListing().getId()).orElseThrow(()->new AppException(ErrorCode.LISTING_NOT_EXISTED));
        if(!listing.getHost().equals(user)){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if(listingAvailabilityRepository.existsByDateAndListingId(request.getDate(), listing.getId())){
            throw new AppException(ErrorCode.LISTING_AVAILABILITY_EXISTED);
        }
        ListingAvailability listingAvailability = ListingAvailability.builder()
                .date(request.getDate())
                .status(request.getStatus())
                .listing(listing)
                .build();
        listingAvailabilityRepository.save(listingAvailability);
        return listingAvailabilityMapper.toListingAvailabilityResponse(listingAvailability);
    }

    @Override
    @PreAuthorize("hasRole('HOST') or hasRole('ADMIN')")
    @Transactional
    public ListingAvailabilityResponse updateListingAvailability(String id, ListingAvailabilityUpdateRequest request) {
        ListingAvailability listingAvailability = listingAvailabilityRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LISTING_AVAILABILITY_NOT_EXISTED));
        verifyHostOfListing(listingAvailability.getListing().getId());
        listingAvailability.setStatus(request.getStatus());
        listingAvailability.setUpdatedAt(LocalDateTime.now());
        listingAvailabilityRepository.save(listingAvailability);
        return listingAvailabilityMapper.toListingAvailabilityResponse(listingAvailability);
    }

    @Override
    @PreAuthorize("hasRole('HOST') or hasRole('ADMIN')")
    @Transactional
    public void createListingAvailabilityForMonth(String listingId, int year, int month) {
        verifyHostOfListing(listingId);
        Listing listing = listingRepository.findById(listingId).orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_EXISTED));
        List<LocalDate> days = generateDaysOfMonth(year, month);
        List<ListingAvailability> availabilities = days.stream()
                .map(day -> {
                    ListingAvailability availability = ListingAvailability.builder()
                            .listing(listing)
                            .date(day)
                            .status(ListingAvailabilityStatus.AVAILABLE)
                            .price(listing.getNightlyPrice())
                            .build();
                    return availability;
                }).toList();
        listingAvailabilityRepository.saveAll(availabilities);
    }

    public List<LocalDate> generateDaysOfMonth(int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<LocalDate> days = new ArrayList<>();
        while (!start.isAfter(end)) {
            days.add(start);
            start = start.plusDays(1);
        }
        return days;
    }

    private User getUserLogin(){
        String username = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        return userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
    private void verifyHostOfListing(String listingId){
        Listing listing = listingRepository.findById(listingId).orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_EXISTED));
        if(!listing.getHost().equals(getUserLogin())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }
}

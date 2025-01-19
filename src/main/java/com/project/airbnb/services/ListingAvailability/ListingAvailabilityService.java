package com.project.airbnb.services.ListingAvailability;

import com.project.airbnb.dtos.request.ListingAvailabilityCreationRequest;
import com.project.airbnb.dtos.request.ListingAvailabilityUpdateRequest;
import com.project.airbnb.dtos.response.ListingAvailabilityResponse;
import com.project.airbnb.dtos.response.PageResponse;
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
        if(!listing.getUser().equals(user)){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if(listingAvailabilityRepository.existsByDateAndListingId(request.getDate(), listing.getId())){
            throw new AppException(ErrorCode.LISTING_AVAILABILITY_EXISTED);
        }
        ListingAvailability listingAvailability = ListingAvailability.builder()
                .date(request.getDate())
                .price(request.getPrice())
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
        listingAvailability.setPrice(request.getPrice());
        listingAvailability.setStatus(request.getStatus());
        listingAvailability.setUpdatedAt(LocalDateTime.now());
        listingAvailabilityRepository.save(listingAvailability);
        return listingAvailabilityMapper.toListingAvailabilityResponse(listingAvailability);
    }
}

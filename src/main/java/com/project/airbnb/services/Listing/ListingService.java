package com.project.airbnb.services.Listing;

import com.project.airbnb.dtos.request.ListingCreationRequest;
import com.project.airbnb.dtos.request.ListingUpdateRequest;
import com.project.airbnb.dtos.response.ListingResponse;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.exceptions.AppException;
import com.project.airbnb.exceptions.ErrorCode;
import com.project.airbnb.mapper.ListingMapper;
import com.project.airbnb.models.*;
import com.project.airbnb.repositories.*;
import com.project.airbnb.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListingService implements IListingService{
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final WardRepository wardRepository;
    private final CategoryRepository categoryRepository;
    private final AmenityRepository amenityRepository;
    private final ListingAvailabilityRepository availabilityRepository;
    private final ListingMapper listingMapper;

    @Override
    public ListingResponse getListingById(String listingId) {
        Listing listing = listingRepository.findById(listingId).orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_EXISTED));
        return listingMapper.toListingResponse(listing);
    }

    @Override
    public PageResponse<List<ListingResponse>> getAllListings(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Listing> listingPage = listingRepository.findAll(pageable);
        List<ListingResponse> listingResponses = listingPage.getContent().stream().map(listingMapper::toListingResponse).toList();

        return PageResponse.<List<ListingResponse>>builder()
                .page(pageable.getPageNumber() + 1)
                .size(pageable.getPageSize())
                .totalElement(listingPage.getTotalElements())
                .totalPage(listingPage.getTotalPages())
                .data(listingResponses)
                .build();
    }

    @Override
    @PreAuthorize("hasRole('HOST') or hasRole('ADMIN')")
    @Transactional
    public ListingResponse createListing(ListingCreationRequest request) {
        String username = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Ward ward = wardRepository.findById(request.getWard().getId()).orElseThrow(() -> new AppException(ErrorCode.WARD_NOT_EXISTED));
        Category category = categoryRepository.findById(request.getCategory().getId()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        Set<String> amenityIds = request.getAmenities().stream()
                .map(Amenity::getId)
                .collect(Collectors.toSet());
        List<Amenity> amenityList = amenityRepository.findAllById(amenityIds);
        Set<Amenity> amenities = new HashSet<>(amenityList);

        Listing listing = Listing.builder()
                .listingName(request.getListingName())
                .nightlyPrice(request.getNightlyPrice())
                .numGuests(request.getNumGuests())
                .numBeds(request.getNumBeds())
                .numBedrooms(request.getNumBedrooms())
                .numBathrooms(request.getNumBathrooms())
                .address(request.getAddress())
                .description(request.getDescription())
                .ward(ward)
                .category(category)
                .user(user)
                .amenities(amenities)
                .build();
        listingRepository.save(listing);
        return listingMapper.toListingResponse(listing);
    }

    @Override
    @PreAuthorize("hasRole('HOST') or hasRole('ADMIN')")
    @Transactional
    public ListingResponse updateListing(String listingId, ListingUpdateRequest request) {
        Listing listing = listingRepository.findById(listingId).orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_EXISTED));
        Ward ward = wardRepository.findById(request.getWard().getId()).orElseThrow(() -> new AppException(ErrorCode.WARD_NOT_EXISTED));
        Category category = categoryRepository.findById(request.getCategory().getId()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        Set<Amenity> amenities = new HashSet<>();
        for(Amenity amenity : request.getAmenities()){
            if(amenityRepository.existsById(amenity.getId())){
                amenities.add(amenity);
            }
        }

        listing.setListingName(request.getListingName());
        listing.setNightlyPrice(request.getNightlyPrice());
        listing.setNumGuests(request.getNumGuests());
        listing.setNumBeds(request.getNumBeds());
        listing.setNumBedrooms(request.getNumBedrooms());
        listing.setNumBathrooms(request.getNumBathrooms());
        listing.setAddress(request.getAddress());
        listing.setDescription(request.getDescription());
        listing.setWard(ward);
        listing.setCategory(category);
        listing.setAmenities(amenities);
        listing.setUpdatedAt(LocalDateTime.now());
        listingRepository.save(listing);
        return listingMapper.toListingResponse(listing);
    }

    @Override
    @PreAuthorize("hasRole('HOST') or hasRole('ADMIN')")
    @Transactional
    public void deleteListing(String listingId) {
        Listing listing = listingRepository.findById(listingId).orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_EXISTED));
        listingRepository.delete(listing);
    }
}

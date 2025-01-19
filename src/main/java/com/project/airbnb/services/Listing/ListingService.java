package com.project.airbnb.services.Listing;

import com.project.airbnb.constants.AppConst;
import com.project.airbnb.dtos.request.ListingCreationRequest;
import com.project.airbnb.dtos.request.ListingUpdateRequest;
import com.project.airbnb.dtos.response.CloudinaryResponse;
import com.project.airbnb.dtos.response.ListingResponse;
import com.project.airbnb.dtos.response.ListingResponseDetail;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.enums.ListingStatus;
import com.project.airbnb.enums.ObjectType;
import com.project.airbnb.exceptions.AppException;
import com.project.airbnb.exceptions.ErrorCode;
import com.project.airbnb.mapper.ListingMapper;
import com.project.airbnb.models.*;
import com.project.airbnb.models.Location.Ward;
import com.project.airbnb.repositories.*;
import com.project.airbnb.repositories.specification.ListingSpecification;
import com.project.airbnb.services.Cloudinary.CloudinaryService;
import com.project.airbnb.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ListingService implements IListingService{
    private final ListingAvailabilityRepository listingAvailabilityRepository;
    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final WardRepository wardRepository;
    private final CategoryRepository categoryRepository;
    private final AmenityRepository amenityRepository;
    private final ListingMapper listingMapper;

    @Override
    public PageResponse<List<ListingResponse>> searchListing(Map<Object, String> filters) {
        int pageNo, pageSize;
        try {
            pageNo = Integer.parseInt(filters.getOrDefault("pageNo", "1"));
            pageSize = Integer.parseInt(filters.getOrDefault("pageSize", "10"));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("NumberFormatException pageNo, pageSize");
        }

        final String keyword = filters.getOrDefault("keyword", null);
        final String categoryName = filters.getOrDefault("categoryName", null);
        Set<String> amenities = null;
        String amenityNames = filters.get("amenities");
        if(amenityNames != null){
            amenities = Arrays.stream(amenityNames.split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet());
        }

        final int numGuests = Integer.parseInt(filters.getOrDefault("numGuests", "1"));
        final Integer numBeds = filters.get("numBeds") != null ? Integer.parseInt(filters.get("numBeds")) : null;
        final Integer numBedrooms = filters.get("numBedrooms") != null ? Integer.parseInt(filters.get("numBedrooms")) : null;
        final Integer numBathrooms = filters.get("numBathrooms") != null ? Integer.parseInt(filters.get("numBathrooms")) : null;
        final BigDecimal minPrice = new BigDecimal(filters.getOrDefault("minPrice", String.valueOf(100000)));
        final BigDecimal maxPrice = new BigDecimal(filters.getOrDefault("maxPrice", String.valueOf(1000000)));
        final LocalDate checkinDate = (filters.get("checkinDate") != null && !filters.get("checkinDate").isEmpty()) ?
                LocalDate.parse(filters.get("checkinDate")) : LocalDate.now().plusDays(1);
        final LocalDate checkoutDate = (filters.get("checkoutDate") != null && !filters.get("checkoutDate").isEmpty()) ?
                LocalDate.parse(filters.get("checkoutDate")) : checkinDate.plusDays(7);

        log.info("keyword: {}, numGuests: {}, checkinDate: {}, checkoutDate: {}, amenities: {}", keyword, numGuests, checkinDate, checkoutDate, amenities);
        log.info("numBeds: {}, numBedrooms: {}, numBathrooms: {}, minPrice: {}, maxPrice: {}", numBeds, numBedrooms, numBathrooms, minPrice, maxPrice);
//        if(!checkinDate.isAfter(LocalDate.now())){
//            throw new IllegalArgumentException("Check-in date must be after today");
//        }
        if(!checkoutDate.isAfter(checkinDate)){
            throw new IllegalArgumentException("Check-out date must be after Check-in date");
        }

        final long totalDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate) + 1;
        List<String> availableListingIds = listingAvailabilityRepository.findAvailableListingIds(
                checkinDate, checkoutDate, totalDays, ListingStatus.AVAILABLE);

        Specification<Listing> spec = Specification.where(ListingSpecification.filterListings(keyword, categoryName, amenities, numBeds, numBedrooms, numBathrooms, numGuests, minPrice, maxPrice))
                .and((root, query, criteriaBuilder) -> root.get("id").in(availableListingIds));
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        Page<Listing> listingPage = listingRepository.findAll(spec, pageable);
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
    public PageResponse<List<ListingResponseDetail>> filterListings(Map<Object, String> filters) {

        return null;
    }

    @Override
    public ListingResponseDetail getListingById(String listingId) {
        Listing listing = listingRepository.findById(listingId).orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_EXISTED));
        return listingMapper.toListingResponseDetail(listing);
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
    public ListingResponseDetail createListing(ListingCreationRequest request) {
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
        return listingMapper.toListingResponseDetail(listing);
    }

    @Override
    @PreAuthorize("hasRole('HOST') or hasRole('ADMIN')")
    @Transactional
    public CloudinaryResponse uploadImage(String listingId, ObjectType objectType, MultipartFile file) throws IOException {
        final int images = imageRepository.countByObjectId(listingId);
        if(images >= AppConst.MAXIMUM_IMAGE_PER_LISTING){
            throw new AppException(ErrorCode.LISTING_IMAGE_MAX_QUANTITY);
        }
        return cloudinaryService.uploadImage(listingId, objectType, file);
    }

    @Override
    @PreAuthorize("hasRole('HOST') or hasRole('ADMIN')")
    @Transactional
    public ListingResponseDetail updateListing(String listingId, ListingUpdateRequest request) {
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
        listing.setStatus(request.getStatus());
        listing.setWard(ward);
        listing.setCategory(category);
        listing.setAmenities(amenities);
        listing.setUpdatedAt(LocalDateTime.now());
        listingRepository.save(listing);
        return listingMapper.toListingResponseDetail(listing);
    }

    @Override
    @PreAuthorize("hasRole('HOST') or hasRole('ADMIN')")
    @Transactional
    public ListingResponseDetail changeStatus(String listingId, Boolean status) {
        Listing listing = listingRepository.findById(listingId).orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_EXISTED));
        listing.setStatus(status);
        listingRepository.save(listing);
        return listingMapper.toListingResponseDetail(listing);
    }

    @Override
    @PreAuthorize("hasRole('HOST') or hasRole('ADMIN')")
    @Transactional
    public void deleteListing(String listingId) {
        Listing listing = listingRepository.findById(listingId).orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_EXISTED));
        listingRepository.delete(listing);
    }
}

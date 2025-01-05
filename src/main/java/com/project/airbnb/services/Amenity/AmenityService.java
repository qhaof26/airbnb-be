package com.project.airbnb.services.Amenity;

import com.project.airbnb.dto.request.AmenityRequest;
import com.project.airbnb.dto.response.AmenityResponse;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.exceptions.AppException;
import com.project.airbnb.exceptions.ErrorCode;
import com.project.airbnb.mapper.AmenityMapper;
import com.project.airbnb.models.Amenity;
import com.project.airbnb.repositories.AmenityRepository;
import com.project.airbnb.repositories.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AmenityService implements IAmenityService{
    private final AmenityRepository amenityRepository;
    private final ListingRepository listingRepository;
    private final AmenityMapper amenityMapper;

    @Override
    public AmenityResponse getAmenityById(String amenityId) {
        return amenityMapper.toAmenityResponse(amenityRepository.findById(amenityId).orElseThrow(() -> new AppException(ErrorCode.AMENITY_NOT_EXISTED)));
    }

    @Override
    public PageResponse<List<AmenityResponse>> getAllAmenities(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        Page<Amenity> amenityPage = amenityRepository.findAll(pageable);
        List<AmenityResponse> amenityResponses = amenityPage.getContent().stream().map(amenityMapper::toAmenityResponse).toList();

        return PageResponse.<List<AmenityResponse>>builder()
                .page(pageable.getPageNumber()+1)
                .size(pageable.getPageSize())
                .totalPage(amenityPage.getTotalPages())
                .totalElement(amenityPage.getTotalElements())
                .data(amenityResponses)
                .build();
    }

    @Override
    public AmenityResponse createAmenity(AmenityRequest request) {
        if(amenityRepository.existsByAmenityName(request.getAmenityName())){
            throw new AppException(ErrorCode.AMENITY_EXISTED);
        }
        Amenity amenity = Amenity.builder()
                .amenityName(request.getAmenityName())
                .build();
        amenityRepository.save(amenity);
        return amenityMapper.toAmenityResponse(amenity);
    }

    @Override
    public AmenityResponse updateAmenity(AmenityRequest request, String amenityId) {
        Amenity amenity = amenityRepository.findById(amenityId).orElseThrow(() -> new AppException(ErrorCode.AMENITY_NOT_EXISTED));
        if(amenityRepository.existsByAmenityName(request.getAmenityName())){
            throw new AppException(ErrorCode.AMENITY_EXISTED);
        }
        amenity.setAmenityName(request.getAmenityName());
        return amenityMapper.toAmenityResponse(amenity);
    }

    @Override
    public void deleteAmenity(String amenityId) {
        Amenity amenity = amenityRepository.findById(amenityId).orElseThrow(() -> new AppException(ErrorCode.AMENITY_NOT_EXISTED));
        amenity.getListings().forEach(listing -> listing.setCategory(null));
        listingRepository.saveAll(amenity.getListings());
        amenityRepository.delete(amenity);
    }
}

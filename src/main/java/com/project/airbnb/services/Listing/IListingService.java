package com.project.airbnb.services.Listing;

import com.project.airbnb.dtos.request.ListingCreationRequest;
import com.project.airbnb.dtos.request.ListingUpdateRequest;
import com.project.airbnb.dtos.response.CloudinaryResponse;
import com.project.airbnb.dtos.response.ListingResponse;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.enums.ObjectType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IListingService {
    ListingResponse getListingById(String listingId); //GUEST, HOST, ADMIN
    PageResponse<List<ListingResponse>> getAllListings(int pageNo, int pageSize); //GUEST, HOST, ADMIN
    ListingResponse createListing(ListingCreationRequest request); //HOST, ADMIN
    CloudinaryResponse uploadImage(String listingId, ObjectType objectType, MultipartFile file) throws IOException; //HOST, ADMIN
    ListingResponse updateListing(String listingId, ListingUpdateRequest request); //HOST, ADMIN
    ListingResponse changeStatus(String listingId, Boolean status); //HOST, ADMIN
    void deleteListing(String listingId); //HOST, ADMIN
}

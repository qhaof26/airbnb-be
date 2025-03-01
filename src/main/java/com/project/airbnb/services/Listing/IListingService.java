package com.project.airbnb.services.Listing;

import com.project.airbnb.dtos.request.ListingCreationRequest;
import com.project.airbnb.dtos.request.ListingUpdateRequest;
import com.project.airbnb.dtos.response.CloudinaryResponse;
import com.project.airbnb.dtos.response.ListingResponse;
import com.project.airbnb.dtos.response.ListingResponseDetail;
import com.project.airbnb.dtos.response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IListingService {
    PageResponse<List<ListingResponse>> searchListings(Map<Object, String> filters);
    PageResponse<List<ListingResponse>> filterListings(Map<Object, String> filters);
    ListingResponseDetail getListingById(String listingId); //GUEST, HOST, ADMIN
    PageResponse<List<ListingResponse>> getAllListings(int pageNo, int pageSize); //GUEST, HOST, ADMIN
    PageResponse<List<ListingResponse>> getAllListingsOfHost(int pageNo, int pageSize); //GUEST, HOST, ADMIN
    ListingResponseDetail createListing(ListingCreationRequest request); //HOST, ADMIN
    CloudinaryResponse uploadImage(String listingId, MultipartFile file) throws IOException; //HOST, ADMIN
    ListingResponseDetail updateListing(String listingId, ListingUpdateRequest request); //HOST, ADMIN
    void deleteListing(String listingId); //HOST, ADMIN
}

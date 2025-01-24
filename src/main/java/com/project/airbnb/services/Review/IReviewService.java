package com.project.airbnb.services.Review;

import com.project.airbnb.dtos.request.ReviewRequest;
import com.project.airbnb.dtos.request.ReviewUpdateRequest;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.dtos.response.ReviewResponse;

import java.util.List;
import java.util.Map;

public interface IReviewService {
    PageResponse<List<ReviewResponse>> getReviewByListing(Map<Object, String> filters);
    ReviewResponse createReview(ReviewRequest request);
    ReviewResponse updateReview(ReviewUpdateRequest request);
    boolean deleteReview(Long reviewId);
}

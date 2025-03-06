package com.project.airbnb.service.Review;

import com.project.airbnb.dto.request.ReviewRequest;
import com.project.airbnb.dto.request.ReviewUpdateRequest;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.dto.response.ReviewResponse;

import java.util.List;
import java.util.Map;

public interface IReviewService {
    PageResponse<List<ReviewResponse>> getReviewByListing(Map<Object, String> filters);
    ReviewResponse createReview(ReviewRequest request);
    ReviewResponse updateReview(ReviewUpdateRequest request);
    boolean deleteReview(Long reviewId);
}

package com.project.airbnb.controller;

import com.project.airbnb.dto.request.ReviewRequest;
import com.project.airbnb.dto.request.ReviewUpdateRequest;
import com.project.airbnb.dto.response.APIResponse;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.dto.response.ReviewResponse;
import com.project.airbnb.service.Review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public APIResponse<PageResponse<List<ReviewResponse>>> getReviewByListing(
            @RequestParam Map<Object, String> filters) {
        return APIResponse.<PageResponse<List<ReviewResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Fetch reviews by listing")
                .data(reviewService.getReviewByListing(filters))
                .build();
    }

    @PostMapping
    public APIResponse<ReviewResponse> createReview(@RequestBody ReviewRequest request) {
        return APIResponse.<ReviewResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created review")
                .data(reviewService.createReview(request))
                .build();
    }

    @PatchMapping
    public APIResponse<ReviewResponse> updateReview(@RequestBody ReviewUpdateRequest request) {
        return APIResponse.<ReviewResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Updated review")
                .data(reviewService.updateReview(request))
                .build();
    }

    @DeleteMapping("/{reviewId}")
    public APIResponse<Boolean> deleteReview(@PathVariable long reviewId) {
        return APIResponse.<Boolean>builder()
                .status(HttpStatus.OK.value())
                .message("Deleted review")
                .data(reviewService.deleteReview(reviewId))
                .build();
    }
}

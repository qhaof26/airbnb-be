package com.project.airbnb.controller;

import com.project.airbnb.dto.request.ReviewRequest;
import com.project.airbnb.dto.request.ReviewUpdateRequest;
import com.project.airbnb.dto.response.APIResponse;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.dto.response.ReviewResponse;
import com.project.airbnb.service.Review.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/reviews")
@Tag(name = "Review")
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "Get review by room", description = "Send a request via this API to get review by room")
    @GetMapping
    public APIResponse<PageResponse<List<ReviewResponse>>> getReviewByListing(
            @RequestParam Map<Object, String> filters) {
        return APIResponse.<PageResponse<List<ReviewResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Fetch reviews by listing successfully")
                .data(reviewService.getReviewByListing(filters))
                .build();
    }

    @Operation(summary = "Create new review", description = "Send a request via this API to create new review")
    @PostMapping
    public APIResponse<ReviewResponse> createReview(@RequestBody ReviewRequest request) {
        return APIResponse.<ReviewResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created review successfully")
                .data(reviewService.createReview(request))
                .build();
    }

    @Operation(summary = "Update review", description = "Send a request via this API to update review")
    @PatchMapping
    public APIResponse<ReviewResponse> updateReview(@RequestBody ReviewUpdateRequest request) {
        return APIResponse.<ReviewResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Updated review successfully")
                .data(reviewService.updateReview(request))
                .build();
    }

    @Operation(summary = "Delete review permanently", description = "Send a request via this API to delete review permanently")
    @DeleteMapping("/{reviewId}")
    public APIResponse<Boolean> deleteReview(@PathVariable long reviewId) {
        return APIResponse.<Boolean>builder()
                .status(HttpStatus.OK.value())
                .message("Deleted review successfully")
                .data(reviewService.deleteReview(reviewId))
                .build();
    }
}

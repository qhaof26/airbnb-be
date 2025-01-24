package com.project.airbnb.controllers;

import com.project.airbnb.dtos.request.ReviewRequest;
import com.project.airbnb.dtos.request.ReviewUpdateRequest;
import com.project.airbnb.dtos.response.APIResponse;
import com.project.airbnb.dtos.response.ListingResponse;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.dtos.response.ReviewResponse;
import com.project.airbnb.services.Review.ReviewService;
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

    @GetMapping("")
    public APIResponse<PageResponse<List<ReviewResponse>>> getReviewByListing(@RequestParam Map<Object, String> filters){
        return APIResponse.<PageResponse<List<ReviewResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Fetch reviews by listing")
                .data(reviewService.getReviewByListing(filters))
                .build();
    }

    @PostMapping("/create")
    public APIResponse<ReviewResponse> createReview(@RequestBody ReviewRequest request){
        return APIResponse.<ReviewResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created review")
                .data(reviewService.createReview(request))
                .build();
    }

    @PatchMapping("")
    public APIResponse<ReviewResponse> updateReview(@RequestBody ReviewUpdateRequest request){
        return APIResponse.<ReviewResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Updated review")
                .data(reviewService.updateReview(request))
                .build();
    }

    @DeleteMapping("/{id}")
    public APIResponse<Boolean> deleteReview(@PathVariable long id){
        return APIResponse.<Boolean>builder()
                .status(HttpStatus.OK.value())
                .message("Deleted review")
                .data(reviewService.deleteReview(id))
                .build();
    }
}

package com.project.airbnb.mapper;

import com.project.airbnb.dto.response.ReviewResponse;
import com.project.airbnb.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public ReviewResponse toReviewResponse(Review review){
        ReviewResponse.User user = new ReviewResponse.User();
        user.setId(review.getUser().getId());
        user.setUsername(review.getUser().getUsername());
        return ReviewResponse.builder()
                .id(review.getId())
                .content(review.getContent())
                .star(review.getStar())
                .user(user)
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}

package com.project.airbnb.services.Review;

import com.project.airbnb.constants.AppConst;
import com.project.airbnb.dtos.request.ReviewRequest;
import com.project.airbnb.dtos.request.ReviewUpdateRequest;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.dtos.response.ReviewResponse;
import com.project.airbnb.enums.BookingStatus;
import com.project.airbnb.exceptions.AppException;
import com.project.airbnb.exceptions.ErrorCode;
import com.project.airbnb.mapper.ReviewMapper;
import com.project.airbnb.models.Listing;
import com.project.airbnb.models.Review;
import com.project.airbnb.models.User;
import com.project.airbnb.repositories.BookingRepository;
import com.project.airbnb.repositories.ListingRepository;
import com.project.airbnb.repositories.ReviewRepository;
import com.project.airbnb.repositories.UserRepository;
import com.project.airbnb.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService implements IReviewService{
    private final ReviewRepository reviewRepository;
    private final ListingRepository listingRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public PageResponse<List<ReviewResponse>> getReviewByListing(Map<Object, String> filters) {
        int pageNo, pageSize;
        try {
            pageNo = Integer.parseInt(filters.getOrDefault("pageNo", "1"));
            pageSize = Integer.parseInt(filters.getOrDefault("pageSize", "10"));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("NumberFormatException pageNo, pageSize");
        }
        String sortBy = filters.get("sortBy");
        final String listingId = filters.get("listing") != null ? filters.get("listing") : null;
        log.info("id: {}, sortBy: {}", listingId,sortBy);
        assert listingId != null;
        if(!listingRepository.existsById(listingId)){
            throw new AppException(ErrorCode.LISTING_NOT_EXISTED);
        }

        Pageable pageable = PageRequest.of(pageNo-1, pageSize, Sort.by(handleSort(sortBy)));
        Page<Review> reviewPage = reviewRepository.findByListingId(listingId, pageable);
        List<ReviewResponse> reviewResponses = reviewPage.getContent().stream().map(reviewMapper::toReviewResponse).toList();

        return PageResponse.<List<ReviewResponse>>builder()
                .page(pageable.getPageNumber()+1)
                .size(pageable.getPageSize())
                .totalElement(reviewPage.getTotalElements())
                .totalPage(reviewPage.getTotalPages())
                .data(reviewResponses)
                .build();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('GUEST')")
    public ReviewResponse createReview(ReviewRequest request) {
        //status = COMPLETED
        //avg = star/quantity user
        User guest = getUserLogin();
        Listing listing = listingRepository.findById(request.getListingId()).orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_EXISTED));
        if(!bookingRepository.existsByListingAndUserAndStatus(listing, guest, BookingStatus.COMPLETED)){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if(request.getStar() > AppConst.MAXIMUM_STAR){
            throw new AppException(ErrorCode.REVIEW_MAX_STAR);
        }
        Review review = Review.builder()
                .content(request.getContent())
                .star(request.getStar())
                .user(guest)
                .listing(listing)
                .build();
        Float averageRating = calculateAverageRating(listing, request.getStar());
        listing.setAverageRating(averageRating);

        reviewRepository.save(review);
        listingRepository.save(listing);
        return reviewMapper.toReviewResponse(review);
    }

    @Override
    @PreAuthorize("hasRole('GUEST')")
    @Transactional
    public ReviewResponse updateReview(ReviewUpdateRequest request) {
        User guest = getUserLogin();
        Review review = reviewRepository.findById(request.getId()).orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXISTED));
        Listing listing = listingRepository.findById(request.getListingId()).orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_EXISTED));
        if(!guest.equals(review.getUser()) || !listing.equals(review.getListing())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        review.setContent(request.getContent());
        review.setStar(request.getStar());
        Float averageRating = calculateAverageRating(listing, request.getStar());
        listing.setAverageRating(averageRating);
        reviewRepository.save(review);
        listingRepository.save(listing);
        return reviewMapper.toReviewResponse(review);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('GUEST') or hasRole('ADMIN')")
    public boolean deleteReview(Long reviewId) {
        User guest = getUserLogin();
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXISTED));
        Listing listing = review.getListing();
        if(!guest.equals(review.getUser())){
            return false;
        }
        reviewRepository.delete(review);
        Float sumStar = reviewRepository.sumStar(listing);
        float averageStar = (sumStar != null) ? sumStar : 0.0f;
        listing.setAverageRating((float) averageStar / reviewRepository.quantityReview(listing));
        listingRepository.save(listing);
        return true;
    }

    private List<Sort.Order> handleSort(String sortBy){
        List<Sort.Order> sorts = new ArrayList<>();
        log.info("sortBy: {}", sortBy);
        if(StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else if (matcher.group(3).equalsIgnoreCase("desc")) {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }
        return sorts;
    }

    private User getUserLogin(){
        String username = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        return userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    private Float calculateAverageRating(Listing listing, Double star){
        Float sumStar = reviewRepository.sumStar(listing);
        float averageStar = (sumStar != null) ? sumStar : 0.0f;
        int quantityReview = reviewRepository.quantityReview(listing);
        if(quantityReview == 0){
            quantityReview = reviewRepository.quantityReview(listing) + 1;
        }
        return (float) ((averageStar + star) / quantityReview);
    }
}

package com.project.airbnb.repository.specification;

import com.project.airbnb.model.Amenity;
import com.project.airbnb.model.Listing;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Set;

public class ListingSpecification {
    // Database: Create index for field name of (Listing, Province, District, Ward)
    public static Specification<Listing> hasCategory(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName == null || categoryName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            root.fetch("category", JoinType.LEFT);
            return criteriaBuilder.equal(root.get("category").get("categoryName"), categoryName);
        };
    }

    public static Specification<Listing> hasAmenities(Set<String> amenityNames) {
        return (root, query, criteriaBuilder) -> {
            if (amenityNames == null || amenityNames.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Listing, Amenity> amenities = root.join("amenities");

            Predicate[] predicates = amenityNames.stream()
                    .map(name -> criteriaBuilder.equal(amenities.get("amenityName"), name))
                    .toArray(Predicate[]::new);

            return criteriaBuilder.and(predicates);
        };
    }

    public static Specification<Listing> hasNumBed(Integer numBeds) {
        return (root, query, criteriaBuilder) -> {
            if (numBeds == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("numBeds"), numBeds);
        };
    }

    public static Specification<Listing> hasNumBedRoom(Integer numBedrooms) {
        return (root, query, criteriaBuilder) -> {
            if (numBedrooms == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("numBedrooms"), numBedrooms);
        };
    }

    public static Specification<Listing> hasNumBathRoom(Integer numBathrooms) {
        return (root, query, criteriaBuilder) -> {
            if (numBathrooms == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("numBathrooms"), numBathrooms);
        };
    }

    public static Specification<Listing> hasGuest(Integer guestCount) {
        return (root, query, criteriaBuilder) -> {
            if (guestCount == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("numGuests"), guestCount);

        };
    }

    public static Specification<Listing> hasPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.and(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("nightlyPrice"), minPrice),
                    criteriaBuilder.lessThanOrEqualTo(root.get("nightlyPrice"), maxPrice));
        };
    }

    // public static Specification<Listing> filterByKeyword(String keyword) {
    // return (root, query, criteriaBuilder) -> {
    // if(keyword == null || keyword.isEmpty()){
    // return criteriaBuilder.conjunction();
    // }
    // Join<Listing, Ward> wardJoin = root.join("ward");
    // Join<Ward, District> districtJoin = wardJoin.join("district");
    // Join<District, Province> provinceJoin = districtJoin.join("province");
    //
    // return criteriaBuilder.or(
    // criteriaBuilder.like(criteriaBuilder.lower(root.get("listingName")), "%" +
    // keyword.toLowerCase() + "%"),
    // criteriaBuilder.like(criteriaBuilder.lower(provinceJoin.get("name")), "%" +
    // keyword.toLowerCase() + "%"),
    // criteriaBuilder.like(criteriaBuilder.lower(districtJoin.get("name")), "%" +
    // keyword.toLowerCase() + "%"),
    // criteriaBuilder.like(criteriaBuilder.lower(wardJoin.get("name")), "%" +
    // keyword.toLowerCase() + "%")
    // );
    // };
    // }

    public static Specification<Listing> filterListings(String keyword, String categoryName, Set<String> amenityNames,
            Integer numBeds, Integer numBedrooms, Integer numBathrooms, Integer guestCount,
            BigDecimal minPrice, BigDecimal maxPrice) {
        return Specification.where(hasGuest(guestCount))
                .and(hasCategory(categoryName))
                .and(hasAmenities(amenityNames))
                .and(hasNumBed(numBeds))
                .and(hasNumBedRoom(numBedrooms))
                .and(hasNumBathRoom(numBathrooms))
                .and(hasPrice(minPrice, maxPrice));
    }

}

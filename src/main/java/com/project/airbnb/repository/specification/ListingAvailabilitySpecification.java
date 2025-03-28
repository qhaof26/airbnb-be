package com.project.airbnb.repository.specification;

import com.project.airbnb.enums.ListingAvailabilityStatus;
import com.project.airbnb.model.Listing;
import com.project.airbnb.model.ListingAvailability;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Objects;

public class ListingAvailabilitySpecification {
    public static Specification<ListingAvailability> hasListing(Listing listing){
        return (root, query, criteriaBuilder) -> {
            if(Objects.isNull(listing)){
                return criteriaBuilder.conjunction();
            }
            root.fetch("category", JoinType.LEFT);
            return criteriaBuilder.equal(root.get("listing"), listing);
        };
    }

    public static Specification<ListingAvailability> isActive(ListingAvailabilityStatus status){
        return (root, query, criteriaBuilder) -> {
          if(Objects.isNull(status)){
              return criteriaBuilder.conjunction();
          }
          return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<ListingAvailability> hasDate(LocalDate checkIn, LocalDate checkOut){
        return (root, query, criteriaBuilder) -> {
            if(Objects.isNull(checkIn) && Objects.isNull(checkOut)){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.and(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("date"), checkIn),
                    criteriaBuilder.lessThanOrEqualTo(root.get("date"), checkOut)
            );
        };
    }

    public static Specification<ListingAvailability> filterAvailability(Listing listing, ListingAvailabilityStatus status, LocalDate checkIn, LocalDate checkOut){
        return Specification.where(hasListing(listing))
                .and(isActive(status))
                .and(hasDate(checkIn, checkOut));
    }
}

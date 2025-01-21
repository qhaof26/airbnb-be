package com.project.airbnb.models;
import com.project.airbnb.enums.ListingAvailabilityStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_listing_availability")
public class ListingAvailability extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ListingAvailabilityStatus status;

//    @Column(name = "price", precision = 15, scale = 2)
//    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;
}

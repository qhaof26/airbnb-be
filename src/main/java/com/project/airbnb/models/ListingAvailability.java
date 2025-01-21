package com.project.airbnb.models;
import com.project.airbnb.enums.ListingAvailabilityStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "listing_availabilities")
public class ListingAvailability extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private ListingAvailabilityStatus status;

    @Column(name = "price", precision = 15, scale = 2)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;
}

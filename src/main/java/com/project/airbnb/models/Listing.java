package com.project.airbnb.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_listing")
public class Listing extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "listing_name", nullable = false)
    private String listingName;

    @Column(name = "nightly_price", precision = 15, scale = 2)
    private BigDecimal nightlyPrice;

    @Column(name = "num_beds")
    private int numBeds;

    @Column(name = "num_bedrooms")
    private int numBedrooms;

    @Column(name = "num_bathrooms")
    private int numBathrooms;

    @Column(name = "num_guests", nullable = false)
    private int numGuests;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "is_guest_favourite")
    private Boolean isGuestFavourite;

    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Ward ward;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "listing")
    private List<ListingAvailability> listingAvailabilities = new ArrayList<>();

    @OneToMany(mappedBy = "listing")
    private List<Booking> bookings = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "listing_amenity",
            joinColumns = @JoinColumn(name = "listing_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private Set<Amenity> amenities = new HashSet<>();
}

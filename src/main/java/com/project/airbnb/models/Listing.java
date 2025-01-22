package com.project.airbnb.models;

import com.project.airbnb.enums.ListingStatus;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "listings")
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

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "geom", columnDefinition = "geometry(Point, 4326)")
    private Point geom;

    @Column(name = "address", unique = true)
    private String address;

    @Column(name = "images", columnDefinition = "text[]")
    @Type(ListArrayType.class)
    private List<String> images = new ArrayList<>();

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private ListingStatus status;

    @Column(name = "average_rating")
    private Float averageRating;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User host;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL)
    private List<ListingAvailability> listingAvailabilities = new ArrayList<>();

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "listing_amenities",
            joinColumns = @JoinColumn(name = "listing_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private Set<Amenity> amenities = new HashSet<>();

    @PrePersist
    @PostLoad
    public void onCreate(){
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        if(averageRating == null){
            averageRating = (float) 0;
        }
    }
}

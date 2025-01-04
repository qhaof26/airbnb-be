package com.project.airbnb.models;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_listing_availability")
public class ListingAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
}

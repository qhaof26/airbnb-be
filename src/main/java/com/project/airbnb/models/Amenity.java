package com.project.airbnb.models;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_amenity")
public class Amenity extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "amenity_name")
    private String amenityName;

    @ManyToMany(mappedBy = "amenities")
    private Set<Listing> listings = new HashSet<>();
}

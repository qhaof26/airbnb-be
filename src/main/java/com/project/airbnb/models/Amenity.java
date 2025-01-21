package com.project.airbnb.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "amenities") //amenities
public class Amenity extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "amenity_name", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    private String amenityName;

    @ManyToMany(mappedBy = "amenities", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Listing> listings = new HashSet<>();
}

package com.project.airbnb.model;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amenity_name", unique = true)
    private String amenityName;

    @ManyToMany(mappedBy = "amenities", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Listing> listings = new HashSet<>();
}

package com.project.airbnb.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_listing")
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;



    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Ward ward;
}

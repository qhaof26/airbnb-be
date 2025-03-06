package com.project.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class Review extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "star", nullable = false)
    private Double star;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;

    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

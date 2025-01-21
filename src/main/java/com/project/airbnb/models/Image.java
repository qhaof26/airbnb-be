package com.project.airbnb.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
public class Image extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "object_id", nullable = false)
    private String objectId;

    @Column(name = "isAvatar")
    private int isAvatar;

    @Column(nullable = false)
    private String url;
}

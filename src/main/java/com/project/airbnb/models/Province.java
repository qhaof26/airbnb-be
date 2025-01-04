package com.project.airbnb.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_province")
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int type;

    @Column(name = "type_text",nullable = false)
    private String typeText;

    @Column(nullable = false)
    private String slug;

    @OneToMany(mappedBy = "province")
    private List<District> districts;
}

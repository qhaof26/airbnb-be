package com.project.airbnb.model;
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
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "permission_name", nullable = false, unique = true)
    private String permissionName;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();
}

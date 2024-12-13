package com.project.airbnb.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

//@Entity
//@Table(name = "users")
//@Data
//public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String firstName;
//    private String lastName;
//
//    @Column(unique = true)
//    private String email;
//
//    private String password;
//    private String phoneNumber;
//
//    @Enumerated(EnumType.STRING)
//    private UserType userType;
//
//    private LocalDate dateOfBirth;
//
//    @CreationTimestamp
//    private LocalDateTime createdAt;
//
//    @UpdateTimestamp
//    private LocalDateTime updatedAt;
//
//    @OneToMany(mappedBy = "host")
//    private List<Listing> listings;
//
//    @OneToMany(mappedBy = "user")
//    private List<Booking> bookings;
//}
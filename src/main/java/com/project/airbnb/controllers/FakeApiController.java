package com.project.airbnb.controllers;

import com.github.javafaker.Faker;
import com.project.airbnb.dtos.request.UserCreationRequest;
import com.project.airbnb.services.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/fake")
public class FakeApiController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @PostMapping
    public ResponseEntity<String> fakeUser(){
        Faker faker = new Faker();
        for(int i = 0; i < 50; i++){
            String password = passwordEncoder.encode("123456");

            UserCreationRequest request = UserCreationRequest.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .username(faker.name().username())
                    .email(faker.internet().emailAddress())
                    .password(password)
                    .build();
            try {
                userService.createNewUser(request);
            } catch (Exception e){
                return ResponseEntity.badRequest().body("Add user fail");
            }
        }
        return ResponseEntity.ok().body("Added user");
    }

}

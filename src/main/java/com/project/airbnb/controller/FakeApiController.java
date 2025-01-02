package com.project.airbnb.controller;

import com.github.javafaker.Faker;
import com.project.airbnb.model.User;
import com.project.airbnb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/fake")
public class FakeApiController {
    public final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<String> fakeUser(){
        Faker faker = new Faker();
        for(int i = 0; i < 1000; i++){
            String email = faker.internet().emailAddress();
            if(userRepository.existsByEmail(email)){
                continue;
            }
            User user = User.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .username(faker.name().username())
                    .email(email)
                    .build();
            try {
                userRepository.save(user);
            } catch (Exception e){
                return ResponseEntity.badRequest().body("Add user fail");
            }
        }
        return ResponseEntity.ok().body("Added user");
    }

}

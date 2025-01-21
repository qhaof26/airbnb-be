package com.project.airbnb.controllers;

import com.github.javafaker.Faker;
import com.project.airbnb.dtos.request.ListingCreationRequest;
import com.project.airbnb.dtos.request.ListingRequest;
import com.project.airbnb.dtos.request.UserCreationRequest;
import com.project.airbnb.exceptions.AppException;
import com.project.airbnb.exceptions.ErrorCode;
import com.project.airbnb.models.Amenity;
import com.project.airbnb.models.Category;
import com.project.airbnb.models.Location.Ward;
import com.project.airbnb.models.Room;
import com.project.airbnb.repositories.AmenityRepository;
import com.project.airbnb.repositories.CategoryRepository;
import com.project.airbnb.repositories.RoomRepository;
import com.project.airbnb.repositories.WardRepository;
import com.project.airbnb.services.Listing.ListingService;
import com.project.airbnb.services.Location.LocationService;
import com.project.airbnb.services.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/fake")
public class FakeApiController {
    private final UserService userService;
    private final ListingService listingService;
    private final WardRepository wardRepository;
    private final LocationService locationService;
    private final CategoryRepository categoryRepository;
    private final AmenityRepository amenityRepository;
    private final PasswordEncoder passwordEncoder;

    private final RoomRepository roomRepository;
    @PostMapping("/user")
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

    @PostMapping("/listing")
    public ResponseEntity<String> fakeListing(){
        Faker faker = new Faker();
        Random random = new Random();
        List<Integer> wardIds = Arrays.asList(
                16, 49, 82, 130, 178, 235, 256, 301, 337
        );
        List<String> categoryIds = Arrays.asList(
               "3f7c534f-0773-499d-88fb-ea84a4f3307d", "67ef9d8a-6bfa-4aef-b054-2da123c9a578", "a011993e-84dd-4bac-a036-e6d44752f164"
        );
        List<String> amenityId = Arrays.asList(
                "ce2ee62b-682f-4287-9807-e086b31bad2d", "b2aee30b-40fc-4e3f-9f1a-fdf8dea9a48f", "94e759ce-d468-4417-92b5-c028926a7e19", "f691bb22-4c68-4d9b-b6a3-823aac72d174"
        );

        Set<Amenity> amenitySet = new HashSet<>();
        for(String i : amenityId){
            Amenity amenity = amenityRepository.findById(i).orElseThrow(()->new AppException(ErrorCode.AMENITY_NOT_EXISTED));
            amenitySet.add(amenity);
        }
        for(int i = 0; i < 10; i++){
            // Random ID from list
            long randomIdWard = wardIds.get(random.nextInt(wardIds.size()));
            String randomIdCate = categoryIds.get(random.nextInt(categoryIds.size()));
            Ward ward = wardRepository.findById(randomIdWard).orElseThrow(() -> new AppException(ErrorCode.WARD_NOT_EXISTED));
            Category category = categoryRepository.findById(randomIdCate).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

            ListingCreationRequest request = ListingCreationRequest.builder()
                    .listingName(faker.company().name())
                    .nightlyPrice(BigDecimal.valueOf(faker.number().randomDouble(2,100000,500000)))
                    .numBeds(faker.number().numberBetween(1, 5))
                    .numBedrooms(faker.number().numberBetween(1, 3))
                    .numBathrooms(faker.number().numberBetween(1, 2))
                    .numGuests(faker.number().numberBetween(1, 6))
                    .description(faker.lorem().sentence(10))
                    .address(faker.address().fullAddress())
                    .ward(ward)
                    .category(category)
                    .amenities(amenitySet)
                    .build();

            try {
                listingService.createListing(request);
            } catch (Exception e){
                return ResponseEntity.badRequest().body("Add listing fail");
            }
        }
        return ResponseEntity.ok().body("Added listing");
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchLocations(@RequestParam String keyword) {
        return ResponseEntity.ok(locationService.searchLocation(keyword));
    }


    @PostMapping("/listings")
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public ResponseEntity<String> createListing(@RequestBody ListingRequest listingRequest) {
        double latitude = listingRequest.getLatitude();
        double longitude = listingRequest.getLongitude();
        String address = listingRequest.getAddress();

        Room room = Room.builder()
                .latitude(latitude)
                .longitude(longitude)
                .address(address)
                .build();
        roomRepository.save(room);
        return ResponseEntity.status(HttpStatus.CREATED).body("lat: " + latitude + " - long: " + longitude + ", address: " + address);
    }

}

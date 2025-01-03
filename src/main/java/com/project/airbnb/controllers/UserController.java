package com.project.airbnb.controllers;

import com.project.airbnb.dto.request.UserCreationRequest;
import com.project.airbnb.dto.response.APIResponse;
import com.project.airbnb.dto.response.UserResponse;
import com.project.airbnb.services.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public APIResponse<UserResponse> fetchUser(@PathVariable String userId){
        return APIResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Fetch user successful")
                .data(userService.fetchUserById(userId))
                .build();
    }




    @PostMapping("/register")
    public APIResponse<UserResponse> createUser(@RequestBody UserCreationRequest request){
        return APIResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created user successful")
                .data(userService.createNewUser(request))
                .build();
    }

    @DeleteMapping("/{userId}")
    public APIResponse<Boolean> removeUser(@PathVariable String userId){
        return APIResponse.<Boolean>builder()
                .status(HttpStatus.OK.value())
                .message("Deleted user")
                .data(userService.removeUser(userId))
                .build();
    }
}

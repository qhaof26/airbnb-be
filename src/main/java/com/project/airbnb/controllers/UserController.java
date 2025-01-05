package com.project.airbnb.controllers;

import com.project.airbnb.dto.request.UserCreationRequest;
import com.project.airbnb.dto.response.APIResponse;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.dto.response.UserResponse;
import com.project.airbnb.services.User.UserService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public APIResponse<UserResponse> getUserById(@PathVariable String userId){
        return APIResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Fetch user successful")
                .data(userService.getUserById(userId))
                .build();
    }

    @GetMapping("/active")
    public APIResponse<PageResponse<List<UserResponse>>> getAllUserActive(
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ){
        return APIResponse.<PageResponse<List<UserResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Fetch all user active successful")
                .data(userService.getAllUserActive(pageNo, pageSize))
                .build();
    }

    @GetMapping("/block")
    public APIResponse<PageResponse<List<UserResponse>>> getAllUserBlock(
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ){
        return APIResponse.<PageResponse<List<UserResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Fetch all user block successful")
                .data(userService.getAllUserBlock(pageNo, pageSize))
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

    @PatchMapping("/change-status/{userId}")
    public APIResponse<UserResponse> changeStatus(@PathVariable String userId, @RequestParam(required = false) boolean status
            ){
        return APIResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Change status user successful")
                .data(userService.changeStatus(userId, status))
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

package com.project.airbnb.controllers;

import com.project.airbnb.dtos.request.UserCreationRequest;
import com.project.airbnb.dtos.response.APIResponse;
import com.project.airbnb.dtos.response.CloudinaryResponse;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.dtos.response.UserResponse;
import com.project.airbnb.services.User.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public APIResponse<UserResponse> getUserById(@PathVariable Long userId){
        return APIResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Fetch user")
                .data(userService.getUserById(userId))
                .build();
    }

    @GetMapping("/active")
    public APIResponse<PageResponse<List<UserResponse>>> getAllUserActive(
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(required = false) String sortBy
    ){
        return APIResponse.<PageResponse<List<UserResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Fetch all user active")
                .data(userService.getAllUserActive(pageNo, pageSize, sortBy))
                .build();
    }

    @GetMapping("/block")
    public APIResponse<PageResponse<List<UserResponse>>> getAllUserBlock(
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(required = false) String sortBy
    ){
        return APIResponse.<PageResponse<List<UserResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Fetch all user block")
                .data(userService.getAllUserBlock(pageNo, pageSize, sortBy))
                .build();
    }

    @GetMapping("/search")
    public APIResponse<PageResponse<List<UserResponse>>> searchUserByKeyword(
            @Min(value = 1) @RequestParam(defaultValue = "1", required = false) int pageNo,
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy

    ){
        return APIResponse.<PageResponse<List<UserResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Search users by keyword")
                .data(userService.searchUsers(pageNo, pageSize, keyword, sortBy))
                .build();
    }

    @GetMapping("/filters")
    public APIResponse<PageResponse<List<UserResponse>>> filterUsers(@RequestParam Map<Object, String> filters){
        return APIResponse.<PageResponse<List<UserResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Filter users")
                .data(userService.filterUsers(filters))
                .build();
    }

    @PostMapping("/create")
    public APIResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request){
        return APIResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created user")
                .data(userService.createNewUser(request))
                .build();
    }

    @PatchMapping("/change-status/{userId}")
    public APIResponse<UserResponse> changeStatus(@PathVariable Long userId, @RequestParam(required = false) boolean status
            ){
        return APIResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Change status user")
                .data(userService.changeStatus(userId, status))
                .build();
    }

    @DeleteMapping("/{userId}")
    public APIResponse<Boolean> removeUser(@PathVariable Long userId){
        return APIResponse.<Boolean>builder()
                .status(HttpStatus.OK.value())
                .message("Deleted user")
                .data(userService.removeUser(userId))
                .build();
    }

    @PostMapping("/images")
    public APIResponse<CloudinaryResponse> uploadImage(
            @RequestParam("id") Long id,
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        return APIResponse.<CloudinaryResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Uploaded avatar")
                .data(userService.uploadAvatar(id, image))
                .build();
    }
}

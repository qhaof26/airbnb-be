package com.project.airbnb.controller;

import com.project.airbnb.dto.request.UserCreationRequest;
import com.project.airbnb.dto.response.APIResponse;
import com.project.airbnb.dto.response.CloudinaryResponse;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.dto.response.UserResponse;
import com.project.airbnb.service.User.UserService;
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

    @PostMapping
    public APIResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request){
        return APIResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created user successfully")
                .data(userService.createNewUser(request))
                .build();
    }

    @PostMapping("/block/{userId}")
    public APIResponse<Boolean> blockUser(@PathVariable Long userId){
        return APIResponse.<Boolean>builder()
                .status(HttpStatus.OK.value())
                .message("Blocked user successfully")
                .data(userService.removeUser(userId))
                .build();
    }

    @PatchMapping("/change-status/{userId}")
    public APIResponse<UserResponse> unBlockUser(@PathVariable Long userId, @RequestParam(required = false) boolean status
    ){
        return APIResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Changed status user successfully")
                .data(userService.changeStatus(userId, status))
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

    @GetMapping("/{userId}")
    public APIResponse<UserResponse> getUserById(@PathVariable Long userId){
        return APIResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Fetch user successfully")
                .data(userService.getUserById(userId))
                .build();
    }

    @PostMapping("/images")
    public APIResponse<CloudinaryResponse> uploadImage(
            @RequestParam("id") Long id,
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        return APIResponse.<CloudinaryResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Uploaded avatar successfully")
                .data(userService.uploadAvatar(id, image))
                .build();
    }
}

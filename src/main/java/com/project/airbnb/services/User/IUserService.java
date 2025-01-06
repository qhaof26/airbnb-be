package com.project.airbnb.services.User;

import com.project.airbnb.dtos.request.UserCreationRequest;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.dtos.response.UserResponse;

import java.util.List;

public interface IUserService {
    UserResponse getUserById(String userId);
    PageResponse<List<UserResponse>> getAllUserActive(int pageNo, int pageSize);
    PageResponse<List<UserResponse>> getAllUserBlock(int pageNo, int pageSize);
    UserResponse createNewUser(UserCreationRequest request);
    UserResponse changeStatus(String userId, Boolean status);
    boolean removeUser(String userId);
}

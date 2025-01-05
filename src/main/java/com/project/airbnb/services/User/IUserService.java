package com.project.airbnb.services.User;

import com.project.airbnb.dto.request.UserCreationRequest;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.dto.response.UserResponse;

import java.util.List;

public interface IUserService {
    UserResponse fetchUserById(String userId);
    PageResponse<List<UserResponse>> fetchAllUserActive(int pageNo, int pageSize);
    PageResponse<List<UserResponse>> fetchAllUserBlock(int pageNo, int pageSize);
    UserResponse createNewUser(UserCreationRequest request);
    UserResponse changeStatus(String userId, Boolean status);
    boolean removeUser(String userId);
}

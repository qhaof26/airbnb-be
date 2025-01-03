package com.project.airbnb.services.User;

import com.project.airbnb.dto.request.UserCreationRequest;
import com.project.airbnb.dto.response.PageResponse;
import com.project.airbnb.dto.response.UserResponse;
import com.project.airbnb.models.User;

import java.util.List;

public interface IUserService {
    UserResponse fetchUserById(String userId);
    PageResponse<List<UserResponse>> fetchAllUser(int pageNo, int pageSize);
    UserResponse createNewUser(UserCreationRequest request);
    boolean removeUser(String userId);
}

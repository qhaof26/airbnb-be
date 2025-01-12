package com.project.airbnb.services.User;

import com.project.airbnb.dtos.request.UserCreationRequest;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.dtos.response.UserResponse;

import java.util.List;

public interface IUserService {
    UserResponse getUserById(String userId); //GUEST, HOST, ADMIN
    PageResponse<List<UserResponse>> getAllUserActive(int pageNo, int pageSize); //ADMIN
    PageResponse<List<UserResponse>> getAllUserBlock(int pageNo, int pageSize); //ADMIN
    UserResponse createNewUser(UserCreationRequest request); //GUEST, HOST, ADMIN
    UserResponse changeStatus(String userId, Boolean status); //ADMIN
    boolean removeUser(String userId); //ADMIN
}

package com.project.airbnb.services.User;

import com.project.airbnb.dtos.request.UserCreationRequest;
import com.project.airbnb.dtos.response.CloudinaryResponse;
import com.project.airbnb.dtos.response.PageResponse;
import com.project.airbnb.dtos.response.UserResponse;
import com.project.airbnb.enums.ImageType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IUserService {
    UserResponse getUserById(Long userId); //ADMIN
    PageResponse<List<UserResponse>> getAllUserActive(int pageNo, int pageSize, String sortBy); //ADMIN
    PageResponse<List<UserResponse>> getAllUserBlock(int pageNo, int pageSize, String sortBy); //ADMIN
    PageResponse<List<UserResponse>> searchUsers(int pageNo, int pageSize, String keyword, String sortBy); //ADMIN
    PageResponse<List<UserResponse>> filterUsers(Map<Object, String> filters); //ADMIN
    UserResponse createNewUser(UserCreationRequest request); //GUEST, HOST, ADMIN
    UserResponse changeStatus(Long userId, Boolean status); //ADMIN
    boolean removeUser(Long userId); //ADMIN
    CloudinaryResponse uploadAvatar(Long userId, MultipartFile file) throws IOException; //HOST, ADMIN
}

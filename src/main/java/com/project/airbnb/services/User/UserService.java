package com.project.airbnb.services.User;

import com.project.airbnb.dto.request.UserCreationRequest;
import com.project.airbnb.dto.response.UserResponse;
import com.project.airbnb.exceptions.AppException;
import com.project.airbnb.exceptions.ErrorCode;
import com.project.airbnb.models.User;
import com.project.airbnb.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;

//    @Override
//    Transactional
//    public UserResponse createNewUser(UserCreationRequest request) {
//        if(userRepository.existsByUsername(request.getUserName())) throw new AppException(ErrorCode.USERNAME_EXISTED);
//
//        if(userRepository.existsByEmail(request.getEmail())) throw new AppException(ErrorCode.EMAIL_EXISTED);
//
//        User newUser = User.builder()
//                .firstName(request.getFirstName())
//                .
//                .build();
//
//        return null;
//    }
}

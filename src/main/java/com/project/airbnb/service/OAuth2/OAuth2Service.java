package com.project.airbnb.service.OAuth2;

import com.project.airbnb.constant.PredefinedRole;
import com.project.airbnb.dto.request.OAuth2UserInfo;
import com.project.airbnb.dto.request.OAuth2UserInfoFactory;
import com.project.airbnb.dto.response.AuthenticationResponse;
import com.project.airbnb.dto.response.UserResponse;
import com.project.airbnb.exception.AppException;
import com.project.airbnb.exception.ErrorCode;
import com.project.airbnb.mapper.UserMapper;
import com.project.airbnb.model.Role;
import com.project.airbnb.model.User;
import com.project.airbnb.repository.RoleRepository;
import com.project.airbnb.repository.UserRepository;
import com.project.airbnb.service.Token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2Service extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final UserMapper userMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return super.loadUser(userRequest);
    }

    @Transactional
    public AuthenticationResponse processOAuthLogin(String registrationId, Map<String, Object> attributes) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);

        if(oAuth2UserInfo.getEmail() == null || oAuth2UserInfo.getEmail().isEmpty()) {
            throw new AppException(ErrorCode.EMAIL_NOT_FOUND_FROM_OAUTH2_PROVIDER);
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isEmpty()) {
            user = createNewUser(oAuth2UserInfo);
        } else {
            user = userOptional.get();
            updateExistingUser(user, oAuth2UserInfo);
        }

        String token = tokenService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

    private User createNewUser(OAuth2UserInfo oAuth2UserInfo) {
        Role guestRole = roleRepository.findByRoleName(PredefinedRole.GUEST_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        Set<Role> roles = new HashSet<>();
        roles.add(guestRole);

        String name = oAuth2UserInfo.getName();
        String firstName = name;
        String lastName = "";

        if (name != null && name.contains(" ")) {
            String[] parts = name.split(" ", 2);
            firstName = parts[0];
            lastName = parts[1];
        }

        User user = User.builder()
                .email(oAuth2UserInfo.getEmail())
                .username(oAuth2UserInfo.getEmail()) // Use email as username
                .firstName(firstName)
                .lastName(lastName)
                .status(true)
                .roles(roles)
                .build();

        return userRepository.save(user);
    }

    private void updateExistingUser(User user, OAuth2UserInfo oAuth2UserInfo) {

        if (user.getFirstName() == null || user.getLastName() == null) {
            String name = oAuth2UserInfo.getName();
            if (name != null && name.contains(" ")) {
                String[] parts = name.split(" ", 2);
                user.setFirstName(parts[0]);
                user.setLastName(parts[1]);
            } else {
                user.setFirstName(name);
            }
            userRepository.save(user);
        }
    }
}

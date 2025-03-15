package com.project.airbnb.configuration.OAuth2;

import com.project.airbnb.constant.PredefinedRole;
import com.project.airbnb.exception.AppException;
import com.project.airbnb.exception.ErrorCode;
import com.project.airbnb.model.Role;
import com.project.airbnb.model.User;
import com.project.airbnb.repository.RoleRepository;
import com.project.airbnb.repository.UserRepository;
import com.project.airbnb.service.Token.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauthToken.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;

        if (userOptional.isEmpty()) {
            // Create new user if not exists
            String firstName = name;
            String lastName = "";
            if (name.contains(" ")) {
                String[] parts = name.split(" ", 2);
                firstName = parts[0];
                lastName = parts[1];
            }

            Role guestRole = roleRepository.findByRoleName(PredefinedRole.GUEST_ROLE)
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

            Set<Role> roles = new HashSet<>();
            roles.add(guestRole);

            user = User.builder()
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .username(email) // Use email as username
                    .status(true)
                    .roles(roles)
                    .build();

            userRepository.save(user);
        } else {
            user = userOptional.get();
        }

        // Generate JWT token
        String token = tokenService.generateToken(user);

        // Redirect to frontend with token
        String redirectUrl = "http://localhost:4200/oauth2/redirect?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}

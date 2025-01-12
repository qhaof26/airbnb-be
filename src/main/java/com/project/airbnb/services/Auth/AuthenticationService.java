package com.project.airbnb.services.Auth;

import com.nimbusds.jose.JOSEException;
import com.project.airbnb.dtos.request.AuthenticationRequest;
import com.project.airbnb.dtos.request.LogoutRequest;
import com.project.airbnb.dtos.response.AuthenticationResponse;
import com.project.airbnb.exceptions.AppException;
import com.project.airbnb.exceptions.ErrorCode;
import com.project.airbnb.models.InvalidatedToken;
import com.project.airbnb.models.Role;
import com.project.airbnb.models.User;
import com.project.airbnb.repositories.InvalidatedTokenRepository;
import com.project.airbnb.repositories.RoleRepository;
import com.project.airbnb.repositories.UserRepository;
import com.project.airbnb.services.Token.TokenService;
import com.project.airbnb.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService{
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Override
    public AuthenticationResponse isAuthenticate(AuthenticationRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String token = tokenService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = tokenService.verifyToken(request.getToken(), true);
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException e){
            log.warn("Token already expired");
        }
    }

    @Override
    @PreAuthorize("hasRole('GUEST')")
    @Transactional
    public void registerHost() {
        String username = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Role roleHost = roleRepository.findByRoleName("HOST").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        Set<Role> roles = user.getRoles();
        roles.add(roleHost);

        user.setDateHostStarted(LocalDate.now());
        user.setRoles(roles);
        userRepository.save(user);
    }
}

package com.project.airbnb.service.Auth;

import com.nimbusds.jose.JOSEException;
import com.project.airbnb.constant.PredefinedRole;
import com.project.airbnb.dto.request.AuthenticationRequest;
import com.project.airbnb.dto.request.LogoutRequest;
import com.project.airbnb.dto.request.RefreshToken;
import com.project.airbnb.dto.request.UserCreationRequest;
import com.project.airbnb.dto.response.AuthenticationResponse;
import com.project.airbnb.dto.response.RegisterResponse;
import com.project.airbnb.exception.AppException;
import com.project.airbnb.exception.ErrorCode;
import com.project.airbnb.mapper.UserMapper;
import com.project.airbnb.model.InvalidatedToken;
import com.project.airbnb.model.Role;
import com.project.airbnb.model.User;
import com.project.airbnb.repository.InvalidatedTokenRepository;
import com.project.airbnb.repository.RoleRepository;
import com.project.airbnb.repository.UserRepository;
import com.project.airbnb.service.Cache.RedisInvalidTokenService;
import com.project.airbnb.service.Email.EmailService;
import com.project.airbnb.service.Token.TokenService;
import com.project.airbnb.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService{
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final RedisInvalidTokenService redisInvalidTokenService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserMapper userMapper;

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
    public RegisterResponse registerAccount(UserCreationRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USERNAME_EXISTED);
        if(userRepository.existsByEmail(request.getEmail())) throw new AppException(ErrorCode.EMAIL_EXISTED);
        String password = passwordEncoder.encode(request.getPassword());
        User newUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(password)
                .email(request.getEmail())
                .build();

        String otp = generateOTP();
        newUser.setOtp(passwordEncoder.encode(otp));
        userRepository.save(newUser);
        sendVerificationEmail(request.getEmail(), otp);

        return userMapper.toRegisterResponse(newUser);
    }

    @Override
    @Transactional
    public boolean verifyAccount(String email, String otp) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(user.getStatus().equals(Boolean.TRUE)){
            throw new AppException(ErrorCode.USER_VERIFIED);
        }
        if(passwordEncoder.matches(otp, user.getOtp())){
            Role role = roleRepository.findByRoleName(PredefinedRole.GUEST_ROLE).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
            Set<Role> roles = new HashSet<>();
            roles.add(role);

            user.setStatus(Boolean.TRUE);
            user.setRoles(roles);
            user.setOtp(null);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshToken token) {
        return null;
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = tokenService.verifyToken(request.getToken(), true);
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
            long ttl = redisInvalidTokenService.calculateTtl(expiryTime);
            redisInvalidTokenService.addToBlacklist(jit, ttl);


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

    private String generateOTP() {
        SecureRandom secureRandom = new SecureRandom();
        int otpValue = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(otpValue);
    }

    private void sendVerificationEmail(String email, String otp){
        String subject = "Email verification";
        String body = "Your verification OTP is: " + otp;
        emailService.sendOtpRegister(email, subject, body);
    }
}

package com.project.airbnb.controller;

import com.nimbusds.jose.JOSEException;
import com.project.airbnb.dto.request.AuthenticationRequest;
import com.project.airbnb.dto.request.LogoutRequest;
import com.project.airbnb.dto.request.RefreshToken;
import com.project.airbnb.dto.request.UserCreationRequest;
import com.project.airbnb.dto.response.APIResponse;
import com.project.airbnb.dto.response.AuthenticationResponse;
import com.project.airbnb.dto.response.RegisterResponse;
import com.project.airbnb.service.Auth.AuthenticationService;
import com.project.airbnb.service.OAuth2.OAuth2Service;
import com.project.airbnb.service.Token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;
    private final OAuth2Service oAuth2Service;

    @PostMapping("/login/{provider}")
    public APIResponse<AuthenticationResponse> loginWithOAuth2(
            @PathVariable("provider") String provider,
            @RequestBody Map<String, Object> userAttributes) {

        AuthenticationResponse response = oAuth2Service.processOAuthLogin(provider, userAttributes);
        return APIResponse.<AuthenticationResponse>builder()
                .status(HttpStatus.OK.value())
                .message("OAuth2 authentication successful")
                .data(response)
                .build();
    }

    @PostMapping("/login")
    public APIResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        AuthenticationResponse response = authenticationService.isAuthenticate(request);
        return APIResponse.<AuthenticationResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Login by username & password successfully")
                .data(response)
                .build();
    }

    @PostMapping("/register")
    public APIResponse<RegisterResponse> registerAccount(@RequestBody UserCreationRequest request){

        return APIResponse.<RegisterResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Register account successfully")
                .data(authenticationService.registerAccount(request))
                .build();
    }

    @PostMapping("/verify")
    public APIResponse<Boolean> verifyAccount(
            @RequestParam("email") String email,
            @RequestParam("otp") String otp
    ){
        return APIResponse.<Boolean>builder()
                .status(HttpStatus.OK.value())
                .message("Verify account successfully")
                .data(authenticationService.verifyAccount(email, otp))
                .build();
    }

    @PostMapping("/register-host")
    public APIResponse<Void> registerHost() {
        authenticationService.registerHost();
        return APIResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Register host airbnb successfully")
                .build();
    }

    @PostMapping("/log-out")
    public APIResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        return APIResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Logout")
                .build();
    }

    @PostMapping("/refresh-token")
    public APIResponse<AuthenticationResponse> refresh(@RequestBody RefreshToken request)
            throws ParseException, JOSEException {
        var result = tokenService.refreshToken(request);
        return APIResponse.<AuthenticationResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Refresh token successfully")
                .data(result)
                .build();
    }
}

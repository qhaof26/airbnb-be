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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
@Tag(name = "Authentication")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;
    private final OAuth2Service oAuth2Service;

    @Operation(method = "POST", summary = "Login with Google", description = "Send a request via this API to login with Google")
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

    @Operation(method = "POST", summary = "Login with username and password", description = "Send a request via this API to login with username and password")
    @PostMapping("/login")
    public APIResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        AuthenticationResponse response = authenticationService.isAuthenticate(request);
        return APIResponse.<AuthenticationResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Login by username & password successfully")
                .data(response)
                .build();
    }

    @Operation(method = "POST", summary = "Register account", description = "Send a request via this API to register account")
    @PostMapping("/register")
    public APIResponse<RegisterResponse> registerAccount(@RequestBody UserCreationRequest request){

        return APIResponse.<RegisterResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Register account successfully")
                .data(authenticationService.registerAccount(request))
                .build();
    }

    @Operation(method = "POST", summary = "Verify account with email OTP", description = "Send a request via this API to verify account with email OTP")
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

    @Operation(method = "POST", summary = "Register to become the host", description = "Send a request via this API to register to become the host")
    @PostMapping("/register-host")
    public APIResponse<Void> registerHost() {
        authenticationService.registerHost();
        return APIResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Register host airbnb successfully")
                .build();
    }

    @Operation(method = "POST", summary = "Logout", description = "Send a request via this API to logout")
    @PostMapping("/log-out")
    public APIResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        return APIResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Logout")
                .build();
    }

    @Operation(method = "POST", summary = "Refresh token", description = "Send a request via this API to refresh token")
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

package com.project.airbnb.controller;

import com.nimbusds.jose.JOSEException;
import com.project.airbnb.dto.request.AuthenticationRequest;
import com.project.airbnb.dto.request.LogoutRequest;
import com.project.airbnb.dto.request.UserCreationRequest;
import com.project.airbnb.dto.response.APIResponse;
import com.project.airbnb.dto.response.AuthenticationResponse;
import com.project.airbnb.dto.response.RegisterResponse;
import com.project.airbnb.service.Auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/log-in")
    public APIResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        AuthenticationResponse response = authenticationService.isAuthenticate(request);
        return APIResponse.<AuthenticationResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Login by username & password")
                .data(response)
                .build();
    }

    @PostMapping("/register")
    public APIResponse<RegisterResponse> registerAccount(@RequestBody UserCreationRequest request){

        return APIResponse.<RegisterResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Register account")
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
                .message("Verify account")
                .data(authenticationService.verifyAccount(email, otp))
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

    @PostMapping("/register-host")
    public APIResponse<Void> registerHost() {
        authenticationService.registerHost();
        return APIResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Register host airbnb")
                .build();
    }
}

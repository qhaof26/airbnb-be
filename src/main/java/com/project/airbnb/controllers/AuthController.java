package com.project.airbnb.controllers;

import com.nimbusds.jose.JOSEException;
import com.project.airbnb.dtos.request.AuthenticationRequest;
import com.project.airbnb.dtos.request.LogoutRequest;
import com.project.airbnb.dtos.response.APIResponse;
import com.project.airbnb.dtos.response.AuthenticationResponse;
import com.project.airbnb.services.Auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

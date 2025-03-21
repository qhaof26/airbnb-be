package com.project.airbnb.service.Auth;

import com.nimbusds.jose.JOSEException;
import com.project.airbnb.dto.request.AuthenticationRequest;
import com.project.airbnb.dto.request.LogoutRequest;
import com.project.airbnb.dto.request.RefreshToken;
import com.project.airbnb.dto.request.UserCreationRequest;
import com.project.airbnb.dto.response.AuthenticationResponse;
import com.project.airbnb.dto.response.RegisterResponse;

import java.text.ParseException;

public interface IAuthenticationService {
    AuthenticationResponse isAuthenticate(AuthenticationRequest request);
    RegisterResponse registerAccount(UserCreationRequest request);
    boolean verifyAccount(String email, String otp);
    void logout(LogoutRequest request) throws ParseException, JOSEException;
    void registerHost(); //GUEST -> HOST
}

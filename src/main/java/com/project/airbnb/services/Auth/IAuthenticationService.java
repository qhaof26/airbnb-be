package com.project.airbnb.services.Auth;

import com.nimbusds.jose.JOSEException;
import com.project.airbnb.dtos.request.AuthenticationRequest;
import com.project.airbnb.dtos.request.LogoutRequest;
import com.project.airbnb.dtos.request.RefreshToken;
import com.project.airbnb.dtos.request.UserCreationRequest;
import com.project.airbnb.dtos.response.AuthenticationResponse;
import com.project.airbnb.dtos.response.RegisterResponse;

import java.text.ParseException;

public interface IAuthenticationService {
    AuthenticationResponse isAuthenticate(AuthenticationRequest request);
    RegisterResponse registerAccount(UserCreationRequest request);
    boolean verifyAccount(String email, String otp);
    AuthenticationResponse refreshToken(RefreshToken token);
    void logout(LogoutRequest request) throws ParseException, JOSEException;
    void registerHost(); //GUEST -> HOST
}

package com.project.airbnb.services.Auth;

import com.nimbusds.jose.JOSEException;
import com.project.airbnb.dtos.request.AuthenticationRequest;
import com.project.airbnb.dtos.request.LogoutRequest;
import com.project.airbnb.dtos.response.AuthenticationResponse;

import java.text.ParseException;

public interface IAuthenticationService {
    AuthenticationResponse isAuthenticate(AuthenticationRequest request);
    void logout(LogoutRequest request) throws ParseException, JOSEException;
}

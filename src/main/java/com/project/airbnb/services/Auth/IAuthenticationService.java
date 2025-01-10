package com.project.airbnb.services.Auth;

import com.project.airbnb.dtos.request.AuthenticationRequest;
import com.project.airbnb.dtos.request.IntrospectRequest;
import com.project.airbnb.dtos.response.AuthenticationResponse;
import com.project.airbnb.dtos.response.IntrospectResponse;

public interface IAuthenticationService {
    AuthenticationResponse isAuthenticate(AuthenticationRequest request);
}

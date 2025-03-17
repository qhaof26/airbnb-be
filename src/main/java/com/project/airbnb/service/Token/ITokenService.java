package com.project.airbnb.service.Token;

import com.nimbusds.jose.JOSEException;
import com.project.airbnb.dto.request.IntrospectRequest;
import com.project.airbnb.dto.request.RefreshToken;
import com.project.airbnb.dto.response.AuthenticationResponse;
import com.project.airbnb.dto.response.IntrospectResponse;
import com.project.airbnb.model.User;

import java.text.ParseException;

public interface ITokenService {
    String generateToken(User user);
    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;
    AuthenticationResponse refreshToken(RefreshToken token) throws ParseException, JOSEException;

}

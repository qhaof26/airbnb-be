package com.project.airbnb.services.Token;

import com.nimbusds.jose.JOSEException;
import com.project.airbnb.dtos.request.IntrospectRequest;
import com.project.airbnb.dtos.response.IntrospectResponse;
import com.project.airbnb.models.User;

import java.text.ParseException;

public interface ITokenService {
    String generateToken(User user);
    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;
    void deleteExpiredTokens();
}

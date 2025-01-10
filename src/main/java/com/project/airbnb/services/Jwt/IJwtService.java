package com.project.airbnb.services.Jwt;

import com.project.airbnb.models.User;

public interface IJwtService {
    String generateToken(User user);
}

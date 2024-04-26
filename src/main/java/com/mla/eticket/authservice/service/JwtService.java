package com.mla.eticket.authservice.service;

import java.util.Map;

public interface JwtService {

    public void validateToken(final String token);

    public String generateToken(String userName);

    public String createToken(Map<String, Object> claims, String userName);
}

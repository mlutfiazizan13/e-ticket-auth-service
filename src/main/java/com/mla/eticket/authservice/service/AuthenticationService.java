package com.mla.eticket.authservice.service;

import com.mla.eticket.authservice.dto.request.LoginRequest;
import com.mla.eticket.authservice.entity.User;

public interface AuthenticationService {

    public User createUser(User user);

    public String generateToken(String userName);
}

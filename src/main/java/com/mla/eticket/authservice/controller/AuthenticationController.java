package com.mla.eticket.authservice.controller;

import com.mla.eticket.authservice.dto.request.LoginRequest;
import com.mla.eticket.authservice.dto.response.LoginResponse;
import com.mla.eticket.authservice.entity.User;
import com.mla.eticket.authservice.service.AuthenticationService;
import com.mla.eticket.commonlib.response.CommonResponse;
import com.mla.eticket.commonlib.response.CommonResponseError;
import com.mla.eticket.commonlib.response.ResponseConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;

    @Value("${jwt.expires.second:3600}")
    private String jwtExpire;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Object> register(@RequestBody User user) {
        CommonResponse<User> response = new CommonResponse<>(user);
        try {
            authenticationService.createUser(user);
        } catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<>(new CommonResponseError(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Object> login (@RequestBody LoginRequest loginRequest) {
        CommonResponse<LoginResponse> response = new CommonResponse<>();
        LoginResponse loginResponse = new LoginResponse();
        try {
            loginResponse.setTokenType("Bearer");
            loginResponse.setExpiresIn(jwtExpire);
            loginResponse.setAccessToken(authenticationService.generateToken(loginRequest.getEmail()));

            response.setData(loginResponse);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new CommonResponseError(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

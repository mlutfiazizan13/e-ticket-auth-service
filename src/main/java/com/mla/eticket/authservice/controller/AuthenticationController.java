package com.mla.eticket.authservice.controller;

import com.mla.eticket.authservice.dto.request.LoginRequest;
import com.mla.eticket.authservice.dto.request.ValidateTokenRequest;
import com.mla.eticket.authservice.dto.response.LoginResponse;
import com.mla.eticket.authservice.entity.User;
import com.mla.eticket.authservice.service.AuthenticationService;
import com.mla.eticket.commonlib.response.CommonResponse;
import com.mla.eticket.commonlib.response.CommonResponseError;
import com.mla.eticket.commonlib.response.ResponseConverter;
import org.bouncycastle.cert.ocsp.Req;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

    private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.expires.second:3600}")
    private String jwtExpire;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, AuthenticationManager authenticationManager) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Object> register(@RequestBody User user) {
        CommonResponse<User> response = new CommonResponse<>("User Register Successfully", user);
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

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        CommonResponse<LoginResponse> response = new CommonResponse<>();
        LoginResponse loginResponse = new LoginResponse();


        if (authenticate.isAuthenticated()) {
            logger.info("Login");
            loginResponse.setTokenType("Bearer");
            loginResponse.setExpiresIn(jwtExpire);
            loginResponse.setAccessToken(authenticationService.generateToken(loginRequest.getEmail()));
            response.setData(loginResponse);
        } else {
            logger.info("Login Gagal");
            return new ResponseEntity<>(new CommonResponseError(), HttpStatus.UNAUTHORIZED);
        }
        logger.info("Login Berhasil");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/validate-token")
    public ResponseEntity<Object> validateToken(@RequestBody ValidateTokenRequest request) {
        logger.info("Cek Token " + request.getToken());
        if (!authenticationService.validateToken(request.getToken())) {
            return new ResponseEntity<>(new CommonResponseError(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new CommonResponse<>(), HttpStatus.OK);
    }
}

package com.mla.eticket.authservice.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String tokenType;
    private String accessToken;
    private String expiresIn;
}

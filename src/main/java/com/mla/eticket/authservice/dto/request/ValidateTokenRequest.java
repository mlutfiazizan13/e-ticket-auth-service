package com.mla.eticket.authservice.dto.request;

import lombok.Data;

@Data
public class ValidateTokenRequest {
    private String token;
}

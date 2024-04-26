package com.mla.eticket.authservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    public static final String SECRET = "Q936ZwAz9/Kn+5pwZfgaYLoZfCbh9EmG3vCYCo6H6kvCdKASrPBrV1Xp2Qod+nqNAIm48mmCzxQLbM794CpsTvwoq/ikDDQtRNGjSlIr6DHl4pUvZRYVqVpet2Qvc6i+NnuKAGc3nq9cbrUtJSdS8WEchIsHEkixU9H9xr94eLrYF+Ka936AY+3CWwvxMYdnIW5SvPe9Znv2otvvVgxlyyKGgDDiB9uwt4xZm7wOUbiv753ks9ZKIk3dLWyO/LMqky8FaLdsPrZnvk51wXNqxpAhuPhcUQaMzZ7lKzu53BdJXP0QcUYOdIMsGXucG0GUSOkSBkZOTALYwCu36x+VzQ==";

    @Value("${jwt.expires.second:3600}")
    private String jwtExpire;

    @Override
    public void validateToken(final String token) {
        Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    @Override
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    @Override
    public String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Integer.parseInt(jwtExpire)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

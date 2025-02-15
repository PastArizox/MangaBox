package com.mangabox.server.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {

    private final String SECRET_KEY;
    private final long EXPIRATION_DATE;

    public JwtUtils(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration}") long expirationDate) {
        this.SECRET_KEY = secretKey;
        this.EXPIRATION_DATE = expirationDate;
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

}

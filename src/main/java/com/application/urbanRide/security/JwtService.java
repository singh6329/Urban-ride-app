package com.application.urbanRide.security;

import com.application.urbanRide.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    SecretKey getSecretKey()
    {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String getAccessToken(User user) {
        return Jwts.builder().subject(user.getId().toString())
                .claim("email",user.getEmail())
                .signWith(getSecretKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60*5))
                .compact();
    }

    public String getRefreshToken(User user) {
       return  Jwts.builder().subject(user.getId().toString())
                .signWith(getSecretKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L *60*60*24*30*6))
                .compact();
    }

    public Long verifyToken(String token) {
        Claims claims = Jwts.parser()
                        .verifyWith(getSecretKey())
                        .build()
                        .parseSignedClaims(token)
                .getPayload();

        return Long.valueOf(claims.getSubject());
    }
}

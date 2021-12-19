package com.learnspringboot.demo.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    @Value("${secret}")
    private String jwtSecret;

    @Value("${expiration}")
    private Long jwtExpriration;

    public String generateJwtToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
        long expiration = (new Date()).getTime() + jwtExpriration;
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", userPrincipal.getUsername());
        payload.put("email", userPrincipal.getEmail());

        return Jwts.builder()
                .setClaims(payload)
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(expiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
package com.backend.votezy20.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey key;

    @PostConstruct
    public void init() {

        key = Keys.hmacShaKeyFor(
                secretKey.getBytes()
        );
    }
    
    public String extractUsername(String token) {

        return extractClaims(token)
                .getSubject();
    }

    public String generateToken(
            String code,
            String role
    ) {

        return Jwts.builder()
                .setSubject(code)
                .claim("role", role)
                .setIssuedAt(
                        new Date()
                )
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + jwtExpiration
                        )
                )
                .signWith(
                        key,
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    public String extractCode(
            String token
    ) {

        return extractClaims(token)
                .getSubject();
    }

    public String extractRole(
            String token
    ) {

        return extractClaims(token)
                .get(
                        "role",
                        String.class
                );
    }

    public boolean isTokenValid(
            String token
    ) {

        try {

            Claims claims =
                    extractClaims(token);

            return claims
                    .getExpiration()
                    .after(
                            new Date()
                    );

        } catch (Exception e) {

            return false;
        }
    }

    private Claims extractClaims(
            String token
    ) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
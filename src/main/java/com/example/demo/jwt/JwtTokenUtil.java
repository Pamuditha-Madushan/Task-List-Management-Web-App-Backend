package com.example.demo.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;

@Service
public class JwtTokenUtil {

    private final JwtConfig jwtConfig;

    private final SecretKey secretKey;

    public JwtTokenUtil(JwtConfig jwtConfig, SecretKey secretKey) {
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    public String generateToken(String username) {


        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now()
                        .plusDays(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(secretKey)
                .compact();

        return token;
    }


    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {

            return false;
        }
    }


}

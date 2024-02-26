package com.compassuol.sp.challenge.msaddress.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
public class JwtTokenService {
    @Value("${security.jwt.token.secret-key:secret}")
    private String SECRET_KEY = "secret";

    @Setter
    private SecretKey encryptedSecretKey;

    @PostConstruct
    private void setup() {
        encryptedSecretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public Claims resolveToken(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(encryptedSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

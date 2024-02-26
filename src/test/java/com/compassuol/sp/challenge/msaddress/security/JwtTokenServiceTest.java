package com.compassuol.sp.challenge.msaddress.security;

import com.compassuol.sp.challenge.msaddress.security.jwt.JwtTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.compassuol.sp.challenge.msaddress.common.AddressUtils.VALID_JWT_SECRETKEY;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class JwtTokenServiceTest {
    @InjectMocks
    private JwtTokenService service;
    private SecretKey encryptedSecretKey;

    @BeforeEach
    public void setup() {
        encryptedSecretKey = Keys.hmacShaKeyFor(VALID_JWT_SECRETKEY.getBytes(StandardCharsets.UTF_8));
        service.setEncryptedSecretKey(encryptedSecretKey);
    }

    private String mockValidAccessToken() {
        final Date issuedAt = new Date();
        final Date expiration = new Date(issuedAt.getTime() + 300000);
        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject("teste@hotmail.com")
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(encryptedSecretKey)
                .claim("role", "USER")
                .compact();
    }

    @Test
    public void resolveToken_WithValidData_ReturnsClaimsResponse() {
        var accessToken = mockValidAccessToken();
        var sut = service.resolveToken(accessToken);

        assertThat(sut).isNotNull();
        assertThat(sut.getSubject()).isEqualTo("teste@hotmail.com");
    }
}

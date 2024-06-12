package com.nightcrowler.spring.banking.security;

import com.nightcrowler.spring.banking.controler.TokenGenerator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Component
@ConditionalOnBean(TokenGenerator.class)
public class JwtGenerator {

    @Value("${security.secret:4f6db2d788a54a9cae790fabea3268f2}")
    private String secret;

    public String generate(String userName, String role) {
        return doGenerateToken(userName, role);
    }

    private String doGenerateToken(String username, String role) {
        return Jwts.builder()
                .claims()
                .add("role", role)
                .subject(username)
                .issuedAt(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
                .expiration(Date.from(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC)))
                .and()
                .signWith(getSigningKey())
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

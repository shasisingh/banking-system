package com.nightcrowler.spring.banking.security;

import com.nightcrowler.spring.banking.controler.TokenGenerator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.BeanInitializationException;
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

    private Key signingKey;

    @PostConstruct
    public void init() {
        if (secret == null || secret.isEmpty()) {
            throw new BeanInitializationException("Secret cannot be empty.");
        }
        signingKey = getSigningKey();
    }

    public String generate(String userName, String role) {
        return doGenerateToken(userName, role);
    }

    private String doGenerateToken(String username, String role) {
        return Jwts.builder()
                .claims()
                .add("role", role)
                .subject(username)
                .issuedAt(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
                .expiration(Date.from(LocalDateTime.now().plusMinutes(10).toInstant(ZoneOffset.UTC)))
                .and()
                .signWith(signingKey)
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

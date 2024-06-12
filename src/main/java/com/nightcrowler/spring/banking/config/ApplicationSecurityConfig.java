package com.nightcrowler.spring.banking.config;

import com.nightcrowler.spring.banking.security.SecurityFilter;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class ApplicationSecurityConfig {
    @Value("${security.secret:4f6db2d788a54a9cae790fabea3268f2}")
    private String secret;

    @Bean
    public SecurityFilter securityFilter() {
        return new SecurityFilter( secretKey());
    }

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}

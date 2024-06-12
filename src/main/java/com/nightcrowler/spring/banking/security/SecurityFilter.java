package com.nightcrowler.spring.banking.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Objects;

/**
 * TODO Class description
 *
 * @author shasisingh
 * @since 12/06/2024
 */
@Slf4j
public class SecurityFilter implements Filter {

    private final SecretKey secret;

    public SecurityFilter(SecretKey secret) {
        this.secret = secret;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        if (canSkipValidation(servletRequest, servletResponse, filterChain)) {
            return;
        }

        var request = (HttpServletRequest) servletRequest;
        var response = (HttpServletResponse) servletResponse;

        String authHeader = getAuthHeader(request);

        if (authHeader.isEmpty()) {
            sendUnauthorized(response, "No Authorization header found");
            return;
        }

        if (!validate(authHeader)) {
            sendForbidden(response, "Unauthorized access. Invalid token.");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private static boolean canSkipValidation(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var request = (HttpServletRequest) servletRequest;
        var requestURI = request.getRequestURI();
        // Ignore /token API
        if (requestURI.contains("/token/generate")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return true;
        }
        return false;
    }


    private void sendUnauthorized(HttpServletResponse response, String messages) throws IOException {
        response.sendError(401, messages);
    }

    private void sendForbidden(HttpServletResponse response, String message) throws IOException {
        response.sendError(403, "");
    }

    private static String getAuthHeader(HttpServletRequest request) {
        if (Objects.nonNull(request.getHeader("Authorization"))) {
            var header = request.getHeader("Authorization");
            if (header.startsWith("Bearer ")) {
                return header.substring(7);
            }
            log.atWarn().addKeyValue("header",header).log("Invalid Authorization header, expected Bearer token");
        }
        return "";
    }

    public boolean validate(String token) {
        log.atTrace().setMessage("Validating token {}").addArgument(token).log();
        try {
            Jwts
                    .parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException ex) {
            log.atError()
                    .setCause(ex)
                    .log("Error validating token");
            return false;
        }
    }


}

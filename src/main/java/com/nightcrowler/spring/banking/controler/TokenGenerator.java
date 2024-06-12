package com.nightcrowler.spring.banking.controler;

import com.nightcrowler.spring.banking.security.JwtGenerator;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@Profile({"dev", "default"})
public class TokenGenerator {

    private final JwtGenerator jwtGenerator;

    public TokenGenerator(JwtGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }

    @RequestMapping("/generate/")
    public ResponseEntity<String> generateToken(@RequestParam String username, @RequestParam String role) {
        return ResponseEntity.ok(jwtGenerator.generate(username, role));
    }
}

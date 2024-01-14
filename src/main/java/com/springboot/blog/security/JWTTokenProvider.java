package com.springboot.blog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.springboot.blog.security.configs.JWTConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.springboot.blog.utils.AppConstants.ROLE_PREFIX;

@Component
@Slf4j
public class JWTTokenProvider {

    private final JWTConfig tokenConfig;

    @Autowired
    public JWTTokenProvider(JWTConfig tokenConfig) {
        this.tokenConfig = tokenConfig;
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getSubject();
    }

    public String generateToken(Authentication authResult) {
        List<String> authorities = authResult.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String userRole = authorities.stream()
                .filter(role -> role.startsWith(ROLE_PREFIX))
                .findAny()
                .orElse("");

        // Remove all values starting with "ROLE_" in authorities
        authorities.removeIf(role -> role.startsWith(ROLE_PREFIX));

        String subject = authResult.getName();
        Date issuedAt = new Date();
        Date expiresAt = Date.from(issuedAt.toInstant().plus(Duration.ofMinutes(tokenConfig.getTokenExpiration())));

        byte[] secretKey = tokenConfig.getSecretKeyForSigning();
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String JWTToken = JWT.create()
                .withSubject(subject)
                .withClaim("role", userRole)
                .withClaim("authorities", authorities)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .sign(algorithm);
        log.info("JWT token is: {}", JWTToken);
        return JWTToken;
    }

    public boolean isValidateToken(String token) {
        byte[] secretKey = tokenConfig.getSecretKeyForSigning();
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
        return true;
    }
}

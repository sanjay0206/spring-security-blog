package com.springboot.blog.security.configs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.jwt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTConfig {
    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpiration;

    public byte[] getSecretKeyForSigning() {
        return secretKey.getBytes();
    }
}
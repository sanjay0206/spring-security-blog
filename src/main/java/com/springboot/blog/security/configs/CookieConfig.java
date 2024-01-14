package com.springboot.blog.security.configs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.cookie")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CookieConfig {
    private String cookieName;
    private Integer cookieMaxAge;
}

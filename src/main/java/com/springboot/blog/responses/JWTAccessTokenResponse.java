package com.springboot.blog.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTAccessTokenResponse {
    private String accessToken;
    private String tokenType;
}

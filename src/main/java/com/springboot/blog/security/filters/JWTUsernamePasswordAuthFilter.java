package com.springboot.blog.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.dto.LoginDto;
import com.springboot.blog.responses.JWTAccessTokenResponse;
import com.springboot.blog.security.JWTTokenProvider;
import com.springboot.blog.security.configs.CookieConfig;
import com.springboot.blog.security.configs.JWTConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JWTUsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {

    protected AuthenticationManager authenticationManager;
    private final JWTConfig tokenConfig;
    private final JWTTokenProvider tokenProvider;
    private final CookieConfig cookieConfig;
    private final ObjectMapper mapper;

    @Autowired
    public JWTUsernamePasswordAuthFilter(AuthenticationManager authenticationManager,
                                         JWTTokenProvider tokenProvider,
                                         JWTConfig tokenConfig,
                                         CookieConfig cookieConfig,
                                         ObjectMapper mapper) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.tokenConfig = tokenConfig;
        this.cookieConfig = cookieConfig;
        this.mapper = mapper;
        setFilterProcessesUrl("/api/v1/auth/signIn");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        Authentication authentication = null;
        try {
            LoginDto loginDto = mapper.readValue(request.getInputStream(), LoginDto.class);
            log.info("Username is: {}", loginDto.getUsernameOrEmail());
            log.info("Password is: {}", loginDto.getPassword());
            authentication = new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(),
                    loginDto.getPassword());

            log.info("Currently logged in user {}", authentication.getPrincipal());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return authenticationManager.authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {

        log.info("Auth result: {}", authResult);
        String token = tokenProvider.generateToken(authResult);
        JWTAccessTokenResponse accessTokenResponse =
                new JWTAccessTokenResponse(token, tokenConfig.getTokenPrefix());

        Cookie clientCookie = new Cookie(cookieConfig.getCookieName(), token);
        clientCookie.setMaxAge(cookieConfig.getCookieMaxAge());

        log.info("Cookie[name={}, value={}]", clientCookie.getName(), clientCookie.getValue());
        response.addHeader(HttpHeaders.COOKIE, token);
        response.addCookie(clientCookie);

        // send JWT token to response body
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), accessTokenResponse);
    }
}

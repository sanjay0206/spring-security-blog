package com.springboot.blog.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.exceptions.BlogAPIException;
import com.springboot.blog.responses.ErrorDetailsResponse;
import com.springboot.blog.security.CustomUserDetailsService;
import com.springboot.blog.security.JWTTokenProvider;
import com.springboot.blog.security.configs.CookieConfig;
import com.springboot.blog.security.configs.JWTConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static com.springboot.blog.utils.AppConstants.URI;

@Component
@Slf4j
public class JWTVerificationFilter extends OncePerRequestFilter {

    private final JWTTokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final JWTConfig tokenConfig;
    private final CookieConfig cookieConfig;
    private final ObjectMapper mapper;

    @Autowired
    public JWTVerificationFilter(CustomUserDetailsService customUserDetailsService,
                                 JWTTokenProvider tokenProvider,
                                 JWTConfig tokenConfig,
                                 CookieConfig cookieConfig,
                                 ObjectMapper mapper) {
        this.tokenProvider = tokenProvider;
        this.customUserDetailsService = customUserDetailsService;
        this.tokenConfig = tokenConfig;
        this.cookieConfig = cookieConfig;
        this.mapper = mapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {

        log.info("Request URL: " + request.getRequestURL());
        try {

            if (!request.getRequestURI().equals("/api/v1/auth/signUp")) {
                String authorizationHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                        .orElseThrow(() -> new BlogAPIException(HttpStatus.FORBIDDEN, "Authorization header not found."));

                Cookie accessTokenFromCookie = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                        .filter(cookie -> cookieConfig.getCookieName().equals(cookie.getName()))
                        .findAny()
                        .orElseThrow(() -> new BlogAPIException(HttpStatus.FORBIDDEN, "Login cookie not found."));

                String tokenPrefix = tokenConfig.getTokenPrefix();
                String accessTokenFromAuthHeader = authorizationHeader.replace(tokenPrefix, "").trim();

                if (accessTokenFromCookie.getValue().equals(accessTokenFromAuthHeader)) {
                    if (tokenProvider.isValidateToken(accessTokenFromAuthHeader)) {
                        // Get username from token and load user details from user service
                        String username = tokenProvider.getUsernameFromToken(accessTokenFromAuthHeader);
                        log.info("username: {}", username);
                        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                        log.info("UserDetails: {}", userDetails);

                        // Create and set authentication token using user details and authorities
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
            // Go to next filter in chain
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            ErrorDetailsResponse errorDetailsResponse = new ErrorDetailsResponse
                    (LocalDateTime.now(), e.getMessage(), URI + request.getRequestURI());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            mapper.writeValue(response.getWriter(), errorDetailsResponse);
        }
    }
}

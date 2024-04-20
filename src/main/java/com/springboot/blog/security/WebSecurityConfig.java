package com.springboot.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.security.configs.CookieConfig;
import com.springboot.blog.security.configs.JWTConfig;
import com.springboot.blog.security.filters.JWTUsernamePasswordAuthFilter;
import com.springboot.blog.security.filters.JWTVerificationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    private final JWTTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final JWTConfig tokenConfig;
    private final CookieConfig cookieConfig;
    private final ObjectMapper mapper;

    @Autowired
    public WebSecurityConfig(CustomUserDetailsService userDetailsService,
                             JWTTokenProvider tokenProvider,
                             JWTConfig tokenConfig,
                             CookieConfig cookieConfig,
                             ObjectMapper mapper) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
        this.tokenConfig = tokenConfig;
        this.cookieConfig = cookieConfig;
        this.mapper = mapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http // by default uses a Bean by the name of corsConfigurationSource
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(getUsernamePasswordAuthFilter())
                .addFilterAfter(getJWTVerificationFilter(), JWTUsernamePasswordAuthFilter.class)
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/v1/auth/**").permitAll()
                .anyRequest()
                .authenticated().and().build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(Collections.singletonList(authProvider));
    }

    @Bean
    public JWTVerificationFilter getJWTVerificationFilter() {
        return new JWTVerificationFilter(userDetailsService, tokenProvider, tokenConfig,  cookieConfig, mapper);
    }

    @Bean
    public JWTUsernamePasswordAuthFilter getUsernamePasswordAuthFilter() {
        return new JWTUsernamePasswordAuthFilter(authenticationManager(), tokenProvider, tokenConfig, cookieConfig, mapper);
    }
}

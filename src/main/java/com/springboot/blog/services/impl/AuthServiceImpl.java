package com.springboot.blog.services.impl;

import com.springboot.blog.dto.SignUpDto;
import com.springboot.blog.entities.User;
import com.springboot.blog.exceptions.BlogAPIException;
import com.springboot.blog.repositories.UserRepository;
import com.springboot.blog.security.configs.CookieConfig;
import com.springboot.blog.security.UserRole;
import com.springboot.blog.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CookieConfig cookieConfig;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, CookieConfig cookieConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cookieConfig = cookieConfig;
    }

    @Override
    public String signUp(SignUpDto signUpDto) {
        // add check for username exists in database
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            throw new BlogAPIException(HttpStatus.FORBIDDEN, "Username is already exists!.");
        }

        // add check for email exists in database
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
        }

        if (signUpDto.getUserRole().equals(UserRole.ADMIN)) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Can not register as admin!.");
        }

        // create user object
        User user = new User();
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setGender(signUpDto.getGender());
        user.setDateOfBirth(signUpDto.getDateOfBirth());
        user.setUserRole(signUpDto.getUserRole());
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return "User registered successfully!.";
    }

    @Override
    public String signOut(HttpServletRequest request, HttpServletResponse response) {
        Optional<Cookie> clientCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> Objects.equals(cookieConfig.getCookieName(), cookie.getName()))
                .findAny();

        clientCookie.ifPresent(cookie -> {
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    log.info("Cookie after signOut: {}", cookie.getValue());
                    response.addHeader(HttpHeaders.COOKIE, cookie.getValue());
                }
        );

        SecurityContextHolder.clearContext();
        return "User logged out successfully";
    }
}

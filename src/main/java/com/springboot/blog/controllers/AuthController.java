package com.springboot.blog.controllers;

import com.springboot.blog.dto.SignUpDto;
import com.springboot.blog.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    @Autowired
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {
        String response = authService.signUp(signUpDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(path = "/signOut")
    public ResponseEntity<?> signOut(HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse) {
        String response = authService.signOut(httpServletRequest, httpServletResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

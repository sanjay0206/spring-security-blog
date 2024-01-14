package com.springboot.blog.services;

import com.springboot.blog.dto.SignUpDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {

    String signUp(SignUpDto signUpDto);

    String signOut(HttpServletRequest request, HttpServletResponse response);
}
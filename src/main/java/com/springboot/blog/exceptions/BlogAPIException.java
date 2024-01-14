package com.springboot.blog.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class BlogAPIException extends IllegalStateException {
    private HttpStatus status;
    private String message;
}

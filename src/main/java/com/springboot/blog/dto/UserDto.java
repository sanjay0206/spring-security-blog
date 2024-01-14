package com.springboot.blog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.blog.entities.User.Gender;
import com.springboot.blog.security.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private LocalDate dateOfBirth;
    private Gender gender;
    private UserRole userRole;
}

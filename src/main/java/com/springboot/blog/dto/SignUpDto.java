package com.springboot.blog.dto;

import com.springboot.blog.entities.User.Gender;
import com.springboot.blog.security.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    // username should not be null or empty
    @NotEmpty(message = "Username should not be null or empty")
    private String username;

    // email should not be null or empty
    // email field validation
    @NotEmpty(message = "Email should not be null or empty")
    @Email
    private String email;

    private String password;

    @PastOrPresent(message = "Date of Birth should not be current date or future date")
    private LocalDate dateOfBirth;
    private Gender gender;

    @NotEmpty(message = "User Role should not be null or empty")
    private UserRole userRole;
}

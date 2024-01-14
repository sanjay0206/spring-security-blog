package com.springboot.blog.controllers;

import com.springboot.blog.dto.UserDto;
import com.springboot.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('SCOPE_user:read')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserInfoById(@PathVariable Long id) {
        UserDto savedUser = userService.getUserById(id);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_user:update')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @PreAuthorize("hasAuthority('SCOPE_user:delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully!.");
    }
}

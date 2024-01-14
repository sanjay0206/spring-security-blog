package com.springboot.blog.services;

import com.springboot.blog.dto.UserDto;

public interface UserService {

    UserDto getUserById(Long userId);

    UserDto updateUser(Long userId, UserDto userDto);

    void deleteUser(Long userId);
}

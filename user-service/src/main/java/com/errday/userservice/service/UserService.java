package com.errday.userservice.service;

import com.errday.userservice.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);
    UserDto findByUserId(String userId);
    List<UserDto> findAll();
}

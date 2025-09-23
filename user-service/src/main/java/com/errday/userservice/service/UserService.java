package com.errday.userservice.service;

import com.errday.userservice.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);
    UserDto findByUserId(String userId);
    List<UserDto> findAll();
    UserDto getUserDetailsByEmail(String email);
}

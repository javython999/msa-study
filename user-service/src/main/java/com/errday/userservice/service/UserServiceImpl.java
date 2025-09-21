package com.errday.userservice.service;

import com.errday.userservice.dto.UserDto;
import com.errday.userservice.jpa.UserEntity;
import com.errday.userservice.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper = new ModelMapper();

    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(bCryptPasswordEncoder.encode(userDto.getPwd()));

        userRepository.save(userEntity);

        return modelMapper.map(userEntity, UserDto.class);

    }

    @Override
    public UserDto findByUserId(String userId) {
        UserEntity findUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found by userId: " + userId));
        return modelMapper.map(findUser, UserDto.class);
    }

    @Override
    public List<UserDto> findAll() {
        List<UserEntity> findAll = userRepository.findAll();
        return findAll.stream()
                .map(userEntity -> modelMapper.map(userEntity, UserDto.class))
                .toList();
    }


}

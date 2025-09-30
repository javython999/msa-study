package com.errday.userservice.service;

import com.errday.userservice.client.OrderServiceClient;
import com.errday.userservice.dto.UserDto;
import com.errday.userservice.jpa.UserEntity;
import com.errday.userservice.jpa.UserRepository;
import com.errday.userservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper = new ModelMapper();
    private final OrderServiceClient orderServiceClient;

    @Override
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

        UserDto userDto = modelMapper.map(findUser, UserDto.class);

        List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);
        userDto.setOrders(orderList);

        return userDto;
    }

    @Override
    public List<UserDto> findAll() {
        List<UserEntity> findAll = userRepository.findAll();
        return findAll.stream()
                .map(userEntity -> modelMapper.map(userEntity, UserDto.class))
                .toList();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found by userId: " + email));
        return modelMapper.map(findUser, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity findUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + username));
        return  new User(findUser.getEmail(), findUser.getEncryptedPwd(), true, true, true, true, new ArrayList<>());
    }
}

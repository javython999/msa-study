package com.errday.userservice.controller;

import com.errday.userservice.dto.UserDto;
import com.errday.userservice.service.UserService;
import com.errday.userservice.vo.Greeting;
import com.errday.userservice.vo.RequestUser;
import com.errday.userservice.vo.ResponseUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
//@RequestMapping("/user-service")
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final Environment env;

    private final Greeting greeting;

    private final UserService userService;

    @GetMapping("/health-check")
    public String status() {
        return new StringBuilder()
                .append("It's Working in User Service").append("\n")
                .append("port(local.server.port) = ").append(env.getProperty("local.server.port")).append("\n")
                .append("port(server.port) = ").append(env.getProperty("server.port")).append("\n")
                .append("gateway ip(env) = ").append(env.getProperty("gateway.ip")).append("\n")
                .append("token secret key = ").append(env.getProperty("token.secret")).append("\n")
                .append("token expiration time = ").append(env.getProperty("token.expiration-time")).append("\n")
                .toString();
    }

    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request) {
        log.info("user.welcome ip: {}, {}, {}, {}", request.getRemoteAddr(), request.getRemoteHost(), request.getRequestURI(), request.getRequestURL());
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> findAll() {
        List<ResponseUser> users = userService.findAll()
                .stream()
                .map(user -> new ModelMapper().map(user, ResponseUser.class))
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> findUser(@PathVariable String userId) {
        UserDto findUser = userService.findByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ModelMapper().map(findUser, ResponseUser.class));
    }
}

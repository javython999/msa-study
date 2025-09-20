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

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final Environment env;

    private final Greeting greeting;

    private final UserService userService;

    @GetMapping("/health-check")
    public String status() {
        return "It's Working in User Service" +
                ", port(local.server.port) = " + env.getProperty("local.server.port") +
                ", port(server.port) =  " + env.getProperty("server.port");
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
}

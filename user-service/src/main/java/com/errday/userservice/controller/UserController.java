package com.errday.userservice.controller;

import com.errday.userservice.dto.UserDto;
import com.errday.userservice.service.UserService;
import com.errday.userservice.vo.Greeting;
import com.errday.userservice.vo.RequestUser;
import com.errday.userservice.vo.ResponseUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Tag(name = "user-controller", description = "일반 사용자 서비스를 위한 컨트롤러 입니다.")
public class UserController {

    private final Environment env;

    private final Greeting greeting;

    private final UserService userService;

    @Operation(summary = "Health check API", description = "Health check를 위한 API (포트 및 Token secret 정보를 확인 가능)")
    @GetMapping("/health-check")
    public Map<String, String> status() {
        Map<String, String> status = new LinkedHashMap<>();
        status.put("message", "It's Working in User Service");
        status.put("local.server.port", env.getProperty("local.server.port"));
        status.put("server.port", env.getProperty("server.port"));
        status.put("gateway.ip", env.getProperty("gateway.ip"));
        status.put("welcome message", env.getProperty("greeting.message"));
        status.put("token.secret", env.getProperty("token.secret"));
        status.put("token.expiration-time", env.getProperty("token.expiration-time"));
        return status;
    }

    @Operation(summary = "환영 메시지 출력 API", description = "Welcome message를 출력하기 위한 API")
    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request) {
        log.info("user.welcome ip: {}, {}, {}, {}", request.getRemoteAddr(), request.getRemoteHost(), request.getRequestURI(), request.getRequestURL());
        return greeting.getMessage();
    }

    @Operation(summary = "사용자 회원가입 API", description = "user-service에 회원 가입을 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR"),
    })
    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @Operation(summary = "전체 사용자 목록조회 API", description = "현재 회원 가입된 전체 사용자 목록을 조회하기 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (인증 실패 오류)"),
            @ApiResponse(responseCode = "403", description = "Forbidden (권한이 없는 페이지에 접근)"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR"),
    })
    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> findAll() {
        List<ResponseUser> users = userService.findAll()
                .stream()
                .map(user -> new ModelMapper().map(user, ResponseUser.class))
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Operation(summary = "사용자 성보 상세조회 API", description = "사용자에 대한 상세 정보를 조회하기 위한 API (사용자 정보 + 주문 내역")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (인증 실패 오류)"),
            @ApiResponse(responseCode = "403", description = "Forbidden (권한이 없는 페이지에 접근)"),
            @ApiResponse(responseCode = "404", description = "Forbidden (권한이 없는 페이지에 접근)"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR"),
    })
    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> findUser(@PathVariable String userId) {
        UserDto findUser = userService.findByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ModelMapper().map(findUser, ResponseUser.class));
    }
}

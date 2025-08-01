package org.example.demo.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.demo.common.util.ResponseUtil;
import org.example.demo.domain.dto.LoginUserDto;
import org.example.demo.domain.dto.RegisterUserDto;
import org.example.demo.domain.emuns.ResponseEnum;
import org.example.demo.domain.entity.User;
import org.example.demo.domain.vo.LoginUserVO;
import org.example.demo.domain.vo.RegisterUserVO;
import org.example.demo.domain.vo.ResponseVO;
import org.example.demo.exception.CustomException;
import org.example.demo.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyun.core.annotation.Query;

import jakarta.validation.Valid;

import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/user")
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final StringRedisTemplate redisTemplate;
    private static final String REGISTER_LIMIT_KEY = "register:limit";
    private static final int REGISTER_LIMIT = 100;
    private static final long LIMIT_WINDOW_SECONDS = 3600; // 1小时

    @PostMapping("/register")
    public ResponseVO<RegisterUserVO> register(@RequestBody @Valid RegisterUserDto registerUserDto) {
        return ResponseUtil.success(userService.register(registerUserDto));
    }

    @PostMapping("/login")
    public ResponseVO<LoginUserVO> login(@RequestBody @Valid LoginUserDto loginUserDto) {
        return ResponseUtil.success(userService.login(loginUserDto));
    }
}

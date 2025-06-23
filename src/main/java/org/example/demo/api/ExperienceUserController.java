package org.example.demo.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.demo.common.util.ResponseUtil;
import org.example.demo.domain.dto.RegisterExperienceUserDto;
import org.example.demo.domain.dto.RegisterUserDto;
import org.example.demo.domain.emuns.ResponseEnum;
import org.example.demo.domain.entity.ExperienceUser;
import org.example.demo.domain.vo.RegisterExperienceUserVO;
import org.example.demo.domain.vo.RegisterUserVO;
import org.example.demo.domain.vo.ResponseVO;
import org.example.demo.exception.CustomException;
import org.example.demo.service.ExperienceUserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/experienceUser")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ExperienceUserController {
    private final ExperienceUserService experienceUserService;
    private final StringRedisTemplate redisTemplate;
    private static final String REGISTER_LIMIT_KEY = "register:limit";
    private static final int REGISTER_LIMIT = 100;
    private static final long LIMIT_WINDOW_SECONDS = 3600; // 1小时

    @PostMapping("/register")
    public ResponseVO<RegisterExperienceUserVO> register(@RequestBody @Valid RegisterExperienceUserDto registerExperienceUserDto) {
        Long count = redisTemplate.opsForValue().increment(REGISTER_LIMIT_KEY);
        if(count == 1) {
            redisTemplate.expire(REGISTER_LIMIT_KEY, LIMIT_WINDOW_SECONDS, TimeUnit.SECONDS);
        }
        if(count > REGISTER_LIMIT) {
            throw new CustomException(ResponseEnum.BAD_REQUEST, "注册人数已满");
        }

        return ResponseUtil.success(experienceUserService.register(registerExperienceUserDto));
    }

    @PostMapping("/resetLimit")
    public ResponseVO<String> resetRegisterLimit() {
        redisTemplate.delete(REGISTER_LIMIT_KEY);
        return ResponseUtil.success("true");
    }

    @PostMapping("/login")
    public ResponseVO<Boolean> login(@RequestBody @Valid RegisterExperienceUserDto registerExperienceUserDto) {
        return ResponseUtil.success(experienceUserService.login(registerExperienceUserDto));
    }

    @GetMapping("/getVerificationCode")
    public ResponseVO<String> getVerificationCode() {
        // 生成6位随机数字
        int code = (int)((Math.random() * 9 + 1) * 100000);
        String verificationCode = String.valueOf(code);
        return ResponseUtil.success(verificationCode);
    }
}

package org.example.demo.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.codehaus.plexus.util.StringUtils;
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
    private static final long LIMIT_WINDOW_SECONDS = 360000; // 1小时

    @PostMapping("/register")
    public ResponseVO<RegisterExperienceUserVO> register(@RequestBody @Valid RegisterExperienceUserDto registerExperienceUserDto) {
        if (StringUtils.isBlank(registerExperienceUserDto.getPhone())) {
            throw new CustomException(ResponseEnum.BAD_REQUEST, "手机号不能为空");
        }
        Long count = redisTemplate.opsForValue().increment(REGISTER_LIMIT_KEY);
        // 如果超过注册人数限制，减少计数
        if(count > REGISTER_LIMIT) {
            redisTemplate.opsForValue().decrement(REGISTER_LIMIT_KEY);
            throw new CustomException(ResponseEnum.BAD_REQUEST, "注册人数已满");
        }
        try {
            RegisterExperienceUserVO registerExperienceUserVO = experienceUserService.register(registerExperienceUserDto);
            if (registerExperienceUserVO == null) {
                // 如果注册失败，减少计数
                redisTemplate.opsForValue().decrement(REGISTER_LIMIT_KEY);
                throw new CustomException(ResponseEnum.BAD_REQUEST, "注册失败");    
            }
            return ResponseUtil.success(registerExperienceUserVO);
        } catch (CustomException e) {
            // 如果发生自定义异常，减少计数
            redisTemplate.opsForValue().decrement(REGISTER_LIMIT_KEY);
            throw e;
        } catch (Exception e) {
            // 如果发生其他异常，减少计数
            redisTemplate.opsForValue().decrement(REGISTER_LIMIT_KEY);
            throw new CustomException(ResponseEnum.INTERNAL_SERVER_ERROR, "注册失败，请稍后再试");
        }
    }

    @PostMapping("/resetLimit")
    public ResponseVO<String> resetRegisterLimit(
            @RequestParam("token") String token,
            @RequestParam("value") String value) {

        // 校验口令
        if (!"3.14159".equals(token)) {
            throw new CustomException(ResponseEnum.BAD_REQUEST, "口令错误");
        }

        // 设置前端传来的值，不设置过期时间
        redisTemplate.opsForValue().set(REGISTER_LIMIT_KEY, value);
        return ResponseUtil.success("设置成功");
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

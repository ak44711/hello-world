package org.example.demo.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.demo.common.util.ResponseUtil;
import org.example.demo.domain.dto.RegisterUserDto;
import org.example.demo.domain.entity.User;
import org.example.demo.domain.vo.RegisterUserVO;
import org.example.demo.domain.vo.ResponseVO;
import org.example.demo.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/user")
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseVO<RegisterUserVO> register(@RequestBody @Valid RegisterUserDto registerUserDto) {
        return ResponseUtil.success(userService.register(registerUserDto));
    }
}

package org.example.demo.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.example.demo.domain.emuns.ResponseEnum;
import org.example.demo.domain.dto.RegisterUserDto;
import org.example.demo.domain.entity.User;
import org.example.demo.domain.vo.RegisterUserVO;
import org.example.demo.exception.CustomException;
import org.example.demo.mapper.UserMapper;
import org.example.demo.service.UserService;
import org.springframework.stereotype.Service;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public RegisterUserVO register(RegisterUserDto registerUserDTO) {
        if(registerUserDTO.getUsername() == null || registerUserDTO.getPassword() == null) {
            throw new CustomException(ResponseEnum.BAD_REQUEST, "账号或密码不能为空");
        }

        User user = new User();
        BeanUtil.copyProperties(registerUserDTO, user, CopyOptions.create().ignoreNullValue());
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));

        userMapper.insert(user);

        RegisterUserVO vo = new RegisterUserVO();
        BeanUtil.copyProperties(user, vo);

        return vo;
    }
}

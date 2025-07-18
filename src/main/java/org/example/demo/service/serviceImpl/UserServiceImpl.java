package org.example.demo.service.serviceImpl;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.example.demo.common.util.JwtUtil;
import org.example.demo.domain.dto.LoginUserDto;
import org.example.demo.domain.emuns.ResponseEnum;
import org.example.demo.domain.dto.RegisterUserDto;
import org.example.demo.domain.entity.User;
import org.example.demo.domain.vo.LoginUserVO;
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

        if (userMapper.existsByUsername(registerUserDTO.getUsername()) ) {
            throw new CustomException(ResponseEnum.BAD_REQUEST, "用户名已存在");
        }

        User user = new User();
        BeanUtil.copyProperties(registerUserDTO, user, CopyOptions.create().ignoreNullValue());
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));

        userMapper.insert(user);

        RegisterUserVO vo = new RegisterUserVO();
        BeanUtil.copyProperties(user, vo);

        return vo;
    }
    @Override
    public LoginUserVO login(LoginUserDto loginUserDTO) {
        User user = userMapper.selectOne(
                // User::getUsername : encoded, loginUserDTO.getUsername(): raw
                new LambdaQueryWrapper<User>().eq(User::getUsername, loginUserDTO.getUsername())
        );

        if(user == null || !passwordEncoder.matches(loginUserDTO.getPassword(), user.getPassword())) {
            throw new CustomException(ResponseEnum.BAD_REQUEST, "账号或密码错误");
        }

        JSONObject tokenData = new JSONObject();
        tokenData.put("id", user.getId());
        tokenData.put("username", loginUserDTO.getUsername());
//        tokenData.put("role", "admin");
        String token = JwtUtil.generateToken(JSON.toJSONString(tokenData));

        LoginUserVO vo = new LoginUserVO();
        BeanUtil.copyProperties(user, vo);
        vo.setToken(token);
        return vo;
    }
}

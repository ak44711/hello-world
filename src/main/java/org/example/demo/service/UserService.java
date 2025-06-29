package org.example.demo.service;
import org.example.demo.domain.dto.LoginUserDto;
import org.example.demo.domain.dto.RegisterUserDto;
import org.example.demo.domain.vo.LoginUserVO;
import org.example.demo.domain.vo.RegisterUserVO;

public interface UserService {
    RegisterUserVO register(RegisterUserDto registerUserDto);
    LoginUserVO login(LoginUserDto loginUserDto);
}

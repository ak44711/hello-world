package org.example.demo.service;
import org.example.demo.domain.dto.RegisterExperienceUserDto;
import org.example.demo.domain.vo.RegisterExperienceUserVO;
import org.example.demo.domain.vo.RegisterUserVO;

public interface ExperienceUserService {
    RegisterExperienceUserVO register(RegisterExperienceUserDto registerExperienceUserDto);
    boolean login(RegisterExperienceUserDto registerExperienceUserDto);
}

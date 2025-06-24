package org.example.demo.service;
import org.example.demo.domain.dto.RegisterExperienceUserDto;
import org.example.demo.domain.entity.ExperienceUser;
import org.example.demo.domain.vo.RegisterExperienceUserVO;
import org.example.demo.domain.vo.RegisterUserVO;

import com.baomidou.mybatisplus.extension.service.IService;

public interface ExperienceUserService {
    RegisterExperienceUserVO register(RegisterExperienceUserDto registerExperienceUserDto);
    boolean login(RegisterExperienceUserDto registerExperienceUserDto);
    boolean deleteById(String id);
}

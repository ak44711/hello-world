package org.example.demo.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.codehaus.plexus.util.StringUtils;
import org.example.demo.domain.dto.RegisterExperienceUserDto;
import org.example.demo.domain.emuns.ResponseEnum;
import org.example.demo.domain.entity.ExperienceUser;
import org.example.demo.domain.vo.RegisterExperienceUserVO;
import org.example.demo.domain.vo.RegisterUserVO;
import org.example.demo.exception.CustomException;
import org.example.demo.mapper.ExperienceUserMapper;
import org.example.demo.service.ExperienceUserService;

import org.springframework.stereotype.Service;
import cn.hutool.core.bean.BeanUtil;

@Service
@RequiredArgsConstructor
public class ExperienceUserServiceImpl implements ExperienceUserService {
    private final ExperienceUserMapper experienceUserMapper;

    @Override
    public RegisterExperienceUserVO register(RegisterExperienceUserDto registerExperienceUserDto) {
        if(StringUtils.isBlank(registerExperienceUserDto.getPhone())) {
            throw new CustomException(ResponseEnum.BAD_REQUEST, "手机号不能为空");
        }

        ExperienceUser experienceUser = new ExperienceUser();
        experienceUser.setPhone(registerExperienceUserDto.getPhone())  ;

        experienceUserMapper.insert(experienceUser);

        RegisterExperienceUserVO vo = new RegisterExperienceUserVO();
        BeanUtil.copyProperties(experienceUser, vo);

        return vo;
    }

    @Override
    public boolean login(RegisterExperienceUserDto registerExperienceUserDto){
        ExperienceUser user = experienceUserMapper.selectOne(
                new LambdaQueryWrapper<ExperienceUser>().eq(ExperienceUser::getPhone, registerExperienceUserDto.getPhone())
        );
        return user != null;
    }
}

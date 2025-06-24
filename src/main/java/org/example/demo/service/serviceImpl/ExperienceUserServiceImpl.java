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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import cn.hutool.core.bean.BeanUtil;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ExperienceUserServiceImpl implements ExperienceUserService {
    private final ExperienceUserMapper experienceUserMapper;
    private final StringRedisTemplate redisTemplate;
    private final ExperienceUserMapper userMapper;

    @Override
    public RegisterExperienceUserVO register(RegisterExperienceUserDto registerExperienceUserDto) {
        
        String lockKey = "register:lock:" + registerExperienceUserDto.getPhone();
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);
        if (locked == null || !locked) {
            throw new CustomException(ResponseEnum.BAD_REQUEST, "请勿重复提交");
        }
        try {
            // 判断手机号是否已存在
            LambdaQueryWrapper<ExperienceUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ExperienceUser::getPhone, registerExperienceUserDto.getPhone());
            ExperienceUser exist = experienceUserMapper.selectOne(queryWrapper);
            if (exist != null) {
                throw new CustomException(ResponseEnum.BAD_REQUEST, "手机号已注册");
            }

            ExperienceUser experienceUser = new ExperienceUser();
            experienceUser.setPhone(registerExperienceUserDto.getPhone());

            experienceUserMapper.insert(experienceUser);
            RegisterExperienceUserVO vo = new RegisterExperienceUserVO();
            BeanUtil.copyProperties(experienceUser, vo);

            return vo;
        }
        finally {
            // 释放锁
            redisTemplate.delete(lockKey);
        }
        
    }

    @Override
    public boolean login(RegisterExperienceUserDto registerExperienceUserDto){
        ExperienceUser user = experienceUserMapper.selectOne(
                new LambdaQueryWrapper<ExperienceUser>().eq(ExperienceUser::getPhone, registerExperienceUserDto.getPhone())
        );
        return user != null;
    }

    @Override
    public boolean deleteById(String id) {
        return userMapper.deleteById(id) > 0;
    }
}

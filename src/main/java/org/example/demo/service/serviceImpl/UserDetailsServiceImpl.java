package org.example.demo.service.serviceImpl;

import org.example.demo.domain.entity.User;
import org.example.demo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userMapper.selectById(userId);
        // 将User转换为Spring Security的UserDetails
        return new org.springframework.security.core.userdetails.User
                (String.valueOf(user.getId()), user.getPassword(), getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        // 根据User的角色构造权限列表
        return Collections.singletonList
                (new SimpleGrantedAuthority("ROLE_" + user.getClass().getSimpleName().toUpperCase()));
    }

}

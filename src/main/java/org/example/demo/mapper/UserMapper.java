package org.example.demo.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.demo.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 通过用户名查询用户是否存在
    @Select("SELECT COUNT(1) > 0 FROM user WHERE username = #{username}")
    boolean existsByUsername(String username);
}

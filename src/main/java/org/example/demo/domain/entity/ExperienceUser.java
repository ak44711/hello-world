package org.example.demo.domain.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.baomidou.mybatisplus.annotation.*; // <groupId>com.baomidou</groupId>


// 自动生成 getter、setter、toString、equals 和 hashCode 方法。
@Data
// 自动生成无参构造方法。
@NoArgsConstructor
// 自动生成包含所有字段的全参构造方法。
@AllArgsConstructor
// 提供 Builder 模式，可以通过链式调用的方式创建对象
@Builder
// MyBatis-Plus 注解 指定数据库表名为 user。
@TableName("experience_user")
public class ExperienceUser {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("phone")
    private String phone;


}

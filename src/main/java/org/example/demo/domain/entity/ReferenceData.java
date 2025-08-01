package org.example.demo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("reference_data")
public class ReferenceData {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String keyword;
    private Integer idx;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
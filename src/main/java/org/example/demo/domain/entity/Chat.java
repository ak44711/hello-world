package org.example.demo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("chat")
public class Chat {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String conversationId;
    private String userId;
    private String role;
    private Timestamp createAt;
    private Long messageId;
}

package org.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.demo.domain.entity.Chat;
import org.example.demo.domain.vo.ChatHistoryVO;

import java.util.List;

@Mapper
public interface IChatMapper extends BaseMapper<Chat> {
    List<ChatHistoryVO> queryChats(
            @Param("username") String username,
            @Param("chatId") Long chatId,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("page") Page<ChatHistoryVO> page
    );
}

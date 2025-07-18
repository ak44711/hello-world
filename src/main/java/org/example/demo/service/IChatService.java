package org.example.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.demo.domain.vo.ChatHistoryVO;
import org.springframework.stereotype.Service;


public interface IChatService {
    IPage<ChatHistoryVO> queryChats(String username, Long chatId, String startTime, String endTime, int pageNum, int pageSize);
}
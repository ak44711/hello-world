package org.example.demo.service.serviceImpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.example.demo.domain.vo.ChatHistoryVO;
import org.example.demo.mapper.IChatMapper;
import org.example.demo.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements IChatService {
    @Autowired
    private IChatMapper chatMapper;

    @Override
    public IPage<ChatHistoryVO> queryChats(String username, Long chatId, String startTime, String endTime, int pageNum, int pageSize) {
        Page<ChatHistoryVO> page = new Page<>(pageNum, pageSize);
        List<ChatHistoryVO> records = chatMapper.queryChats(username, chatId, startTime, endTime, page);
        page.setRecords(records);
        return page;
    }
}

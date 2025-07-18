package org.example.demo.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.example.demo.domain.vo.ChatHistoryVO;
import org.example.demo.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private IChatService chatService;

    @GetMapping("/query")
    public IPage<ChatHistoryVO> queryChats(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Long chatId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return chatService.queryChats(username, chatId, startTime, endTime, pageNum, pageSize);
    }
}
package org.example.demo.domain.vo;

import lombok.Data;

@Data
public class ChatHistoryVO {
    private Long chatId;
    private String username;
    private String role;
    private String createAt;
    private String content;
    // getters and setters
}

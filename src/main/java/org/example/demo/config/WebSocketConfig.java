package org.example.demo.config;

import org.example.demo.socket.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;


@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 1) 聊天
        registry.addHandler(chatWebSocketHandler, "/ws/chat")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .addInterceptors(new JwtHandshakeInterceptor())
                .setAllowedOrigins("*");
    }
}

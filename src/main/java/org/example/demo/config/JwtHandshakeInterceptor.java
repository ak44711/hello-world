package org.example.demo.config;

import org.example.demo.common.util.JwtUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.server.HandshakeInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.socket.WebSocketHandler;
import java.util.Map;

import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String token = null;
        if (request instanceof org.springframework.http.server.ServletServerHttpRequest servletRequest) {
            var httpRequest = servletRequest.getServletRequest();
            token = httpRequest.getParameter("Authorization");
        }
        if (token == null || !JwtUtil.validateToken(token)) {
            response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            // 返回 false，终止握手
            return false;
        }
        attributes.put("userId", JwtUtil.extractUserId(token));
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // 可留空
    }
}
package org.example.demo.socket;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Value("${coze.botid}")
    String botid;
    @Value("${coze.token}")
    String cozeToken;
    @Value("${coze.base_url}")
    String cozeBaseUrl;

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .build();

    private final ObjectMapper objectMapper;



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Object userId = session.getAttributes().get("userId");
        log.warn("用户连接成功");
    }

    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.warn("用户断开连接");
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("发送了消息：{}",payload);

        String content = "请介绍你自己";
        String userId = "hp123";
        if ("".equals(content) || "".equals(userId) ) {
            session.sendMessage(new TextMessage("请输入店铺名称和商品"));
            session.sendMessage(new TextMessage("[DONE]"));
        }

        String json = "{"
                + "\"parameters\":{},"
                + "\"bot_id\":\"" + botid + "\","
                + "\"user_id\":\"" + userId + "\","
                + "\"stream\":true,"
                + "\"additional_messages\":[{\"content\":\"" + content + "\",\"content_type\":\"text\",\"role\":\"user\",\"type\":\"question\"}]"
                + "}";

        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(cozeBaseUrl)
                .addHeader("Authorization", "Bearer " + cozeToken)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        StringBuilder buffer = new StringBuilder();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                session.sendMessage(new TextMessage("[AI接口异常]"));
                return;
            }
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // 只处理以 data: 开头的行
                    if (line.startsWith("data:")) {
                        String jsonPart = line.substring(5).trim();
                        if ("[DONE]".equals(jsonPart)) break;
                        try {
                            JsonNode node = objectMapper.readTree(jsonPart);
                            // 只处理文本内容
                            // ...existing code...
                            if (node.has("type") && "answer".equals(node.get("type").asText())) {
                                String contentText = node.get("content").asText("");
                                String result = "{content:\"" + contentText + "\", type:\"answer\"}";
                                session.sendMessage(new TextMessage(result));
//                                System.out.print(result);
                            }
                            if (node.has("type") && "follow_up".equals(node.get("type").asText())) {
                                String contentText = node.get("content").asText("");
                                String result = "{content:\"" + contentText + "\", type:\"follow_up\"}";
                                session.sendMessage(new TextMessage(result));
//                                System.out.print(result);
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                session.sendMessage(new TextMessage("[DONE]"));
            }
        } catch (Exception e) {
            log.error("流式解析失败", e);
            session.sendMessage(new TextMessage("[AI回答异常]"));
        }
    }
}

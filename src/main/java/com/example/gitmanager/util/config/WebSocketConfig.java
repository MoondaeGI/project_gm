package com.example.gitmanager.util.config;

import com.example.gitmanager.chat.endpoint.ChatEndPoint;
import com.example.gitmanager.git.endpoint.GitEndPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatEndPointHandler(), "/ws/chat")
                .addHandler(gitEndPointHandler(), "/ws/git")
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler chatEndPointHandler() {
        return new ChatEndPoint();
    }

    @Bean
    public WebSocketHandler gitEndPointHandler() {
        return new GitEndPoint();
    }
}

package com.safevillage.safevillage.domain.reports.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class analyzeConfig {
    @Value("${spring.ai.openai.api-key}")
    private String apikey;

    @Bean
    public ChatModel chatModel(){
        return new OpenAiChatModel(new OpenAiApi(apikey));
    }

    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }
}
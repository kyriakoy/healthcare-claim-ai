package com.example.healthcareclaims.service;

// import org.springframework.ai.chat.ChatClient;
// import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

// @Service
// public class AiService {

//     private final ChatClient chatClient;

//     public AiService(ChatClient chatClient) {
//         this.chatClient = chatClient;
//     }

//     public String generateResponse(String prompt) {
//         ChatResponse response = chatClient.call(new Prompt(new UserMessage(prompt)));
//         return response.getResult().getOutput().getContent();
//     }
// } 
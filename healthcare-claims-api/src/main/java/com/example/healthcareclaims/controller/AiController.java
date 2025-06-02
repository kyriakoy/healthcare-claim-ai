package com.example.healthcareclaims.controller;

// import com.example.healthcareclaims.service.AiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// @RestController
// @RequestMapping("/api/ai")
// public class AiController {

//     private final AiService aiService;

//     public AiController(AiService aiService) {
//         this.aiService = aiService;
//     }

//     @PostMapping("/chat")
//     public Map<String, String> chat(@RequestBody Map<String, String> request) {
//         String prompt = request.getOrDefault("prompt", "");
//         String response = aiService.generateResponse(prompt);
//         return Map.of("response", response);
//     }
// } 
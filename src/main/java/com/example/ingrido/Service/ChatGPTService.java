package com.example.ingrido.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class ChatGPTService {

    private final String apiKey = "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";

    private final String endpoint = "https://api.groq.com/openai/v1/chat/completions"; // Groq API endpoint

    public String askGroq(String question) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey); // Groq API Key in Authorization header

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama3-70b-8192"); // Best Groq model
        body.put("messages", List.of(
                Map.of("role", "system", "content", "You are a helpful chef assistant. You help people cook and suggest meals."),
                Map.of("role", "user", "content", question)
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // Groq API response
        ResponseEntity<Map> response = restTemplate.postForEntity(endpoint, request, Map.class);

        // Extracting response data
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

        return (String) message.get("content"); // The AI's response to the user's question
    }
}

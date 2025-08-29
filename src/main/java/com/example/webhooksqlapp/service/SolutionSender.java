package com.example.webhooksqlapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SolutionSender {

    private static final Logger log = LoggerFactory.getLogger(SolutionSender.class);
    private final RestTemplate restTemplate;

    public SolutionSender(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Sends the SQL solution and JWT token to the specified webhook URL.
     *
     * @param webhookUrl    The URL to send the solution to.
     * @param solutionQuery The SQL query that was solved.
     * @param jwtToken      The JWT token for authentication/authorization.
     */
    public void sendSolution(String webhookUrl, String solutionQuery, String jwtToken) {
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            log.error("Cannot send solution: Webhook URL is null or empty.");
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Add the JWT token to the Authorization header
        headers.setBearerAuth(jwtToken);
        // or headers.set("Authorization", "Bearer " + jwtToken);

        Map<String, String> payload = new HashMap<>();
        payload.put("solutionQuery", solutionQuery);
        payload.put("timestamp", String.valueOf(System.currentTimeMillis()));
        // You can add more data here if the webhook expects it, e.g., problemId

        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

        try {
            log.info("Sending solution to webhook {}: {}", webhookUrl, payload);
            ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, request, String.class);
            log.info("Solution sent to webhook. Status Code: {}, Response: {}", response.getStatusCode(), response.getBody());
        } catch (Exception e) {
            log.error("Error sending solution to webhook {}: {}", webhookUrl, e.getMessage(), e);
        }
    }
}

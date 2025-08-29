package com.example.webhooksqlapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.Objects; // For Objects.requireNonNull

@Service
public class WebhookService {

    private static final Logger log = LoggerFactory.getLogger(WebhookService.class);
    private final RestTemplate restTemplate;

    // Injects the RestTemplate bean defined in WebhookSqlAppApplication
    public WebhookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Simulates sending a POST request to a webhook generation service.
     * In a real scenario, this would call an actual external API.
     * For this example, it uses a placeholder service URL that might
     * return a dummy webhook URL.
     * @return The generated webhook URL, or null if generation fails.
     */
    public String generateWebhook() {
        // This is a placeholder URL for the service that generates webhooks.
        // In a real application, you'd replace this with an actual API endpoint.
        // For demonstration, we'll assume a dummy JSON response like:
        // {"webhookUrl": "http://localhost:8080/my-mock-webhook"}
        String webhookGeneratorUrl = "https://mock-webhook-generator.example.com/generate"; // Replace with actual URL

        try {
            // We're sending an empty request body for simplicity.
            // A real service might require specific parameters.
            log.info("Sending POST request to webhook generator: {}", webhookGeneratorUrl);
            ResponseEntity<Map> response = restTemplate.postForEntity(webhookGeneratorUrl, null, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // Assuming the response body contains a "webhookUrl" field
                String generatedUrl = (String) response.getBody().get("webhookUrl");
                if (generatedUrl != null && !generatedUrl.isEmpty()) {
                    log.info("Successfully received webhook URL: {}", generatedUrl);
                    return generatedUrl;
                } else {
                    log.warn("Webhook generation service returned OK but no 'webhookUrl' in response body.");
                }
            } else {
                log.error("Failed to generate webhook. Status code: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error generating webhook: {}", e.getMessage(), e);
        }
        // If anything goes wrong, return null
        return null;
    }
}

package com.example.webhooksqlapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate; // Needed for making HTTP requests

// Required for JWT token generation
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

// For database interaction
import org.springframework.jdbc.core.JdbcTemplate;
import lombok.RequiredArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

// For logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootApplication
public class WebhookSqlAppApplication {

	private static final Logger log = LoggerFactory.getLogger(WebhookSqlAppApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WebhookSqlAppApplication.class, args);
	}

	// This bean will be used to make HTTP requests
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	// CommandLineRunner will execute logic when the application starts
	@Bean
	public CommandLineRunner run(
			WebhookService webhookService,
			SqlProblemSolver sqlProblemSolver,
			JwtTokenGenerator jwtTokenGenerator,
			SolutionSender solutionSender) {
		return args -> {
			log.info("Application starting up and performing tasks...");

			// 1. Send POST request to generate a webhook
			log.info("Attempting to generate webhook...");
			String webhookUrl = webhookService.generateWebhook();

			if (webhookUrl != null) {
				log.info("Webhook generated successfully: {}", webhookUrl);

				// 2. Based on the response, solve a SQL problem and store the result
				log.info("Solving SQL problem and storing result...");
				Long problemId = 1L; // Example: solving the first problem
				String solvedQuery = sqlProblemSolver.solveProblem(problemId);

				if (solvedQuery != null) {
					log.info("SQL Problem solved. Generated query: {}", solvedQuery);
					// Store the solution (already done within solveProblem)

					// 3. Send the solution to the returned webhook URL using a JWT token
					log.info("Generating JWT token...");
					String jwtToken = jwtTokenGenerator.generateToken("sql_solver", "user@example.com");
					log.info("JWT Token generated.");

					log.info("Sending solution to webhook URL: {}", webhookUrl);
					solutionSender.sendSolution(webhookUrl, solvedQuery, jwtToken);
					log.info("Solution sending initiated.");
				} else {
					log.error("Failed to solve SQL problem or retrieve generated query.");
				}
			} else {
				log.error("Failed to generate webhook. Aborting subsequent steps.");
			}

			log.info("Application tasks completed.");
		};
	}
}
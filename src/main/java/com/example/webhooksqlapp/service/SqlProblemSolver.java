package com.example.webhooksqlapp.service;

import com.example.webhooksqlapp.model.Problem;
import com.example.webhooksqlapp.repository.ProblemRepository;
import com.example.webhooksqlapp.repository.SolutionRepository;
import com.example.webhooksqlapp.model.Solution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SqlProblemSolver {

    private static final Logger log = LoggerFactory.getLogger(SqlProblemSolver.class);
    private final ProblemRepository problemRepository;
    private final SolutionRepository solutionRepository;

    public SqlProblemSolver(ProblemRepository problemRepository, SolutionRepository solutionRepository) {
        this.problemRepository = problemRepository;
        this.solutionRepository = solutionRepository;
    }

    /**
     * This method simulates solving a SQL problem based on its ID.
     * In a real application, this would involve more complex logic,
     * possibly parsing the 'data' field from the Problem entity
     * and generating an appropriate SQL query.
     *
     * For this example, we simply return a hardcoded SQL query
     * based on the problem ID and store it.
     *
     * @param problemId The ID of the SQL problem to solve.
     * @return The "solved" SQL query as a String, or null if the problem is not found.
     */
    public String solveProblem(Long problemId) {
        Optional<Problem> optionalProblem = problemRepository.findById(problemId);

        if (optionalProblem.isPresent()) {
            Problem problem = optionalProblem.get();
            log.info("Attempting to solve problem ID {}: {}", problem.getId(), problem.getQuestion());

            String solvedQuery;
            // This is where your SQL problem-solving logic would go.
            // For a real application, you might use AI, a rule engine,
            // or complex parsing of the 'problem.getData()' to formulate the query.
            if (problem.getId() == 1L) {
                solvedQuery = "SELECT * FROM orders WHERE order_date >= DATEADD('MONTH', -1, CURRENT_DATE())";
            } else if (problem.getId() == 2L) {
                solvedQuery = "SELECT * FROM products WHERE stock > 50";
            } else {
                solvedQuery = "SELECT 'No specific solution for this problem ID' AS solution";
            }

            // Store the solution in the database
            Solution solution = new Solution();
            solution.setProblemId(problem.getId());
            solution.setSolutionQuery(solvedQuery);
            solutionRepository.save(solution);
            log.info("Solution for problem ID {} stored successfully.", problem.getId());

            return solvedQuery;
        } else {
            log.error("SQL Problem with ID {} not found.", problemId);
            return null;
        }
    }
}

// src/main/java/com/example/webhooksqlapp/service/JwtTokenGenerator.java
package com.example.webhooksqlapp.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenGenerator {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenGenerator.class);
    // IMPORTANT: In a real application, this key should be loaded securely
    // from environment variables or a key vault, and should be strong (e.g., 256-bit).
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Generates a JWT token with specified subject and audience.
     *
     * @param subject   The subject of the token (e.g., user ID or service name).
     * @param audience  The audience of the token (e.g., the service it's intended for).
     * @return A signed JWT token string.
     */
    public String generateToken(String subject, String audience) {
        log.info("Generating JWT token for subject: {} and audience: {}", subject, audience);

        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        // Token expires in 1 hour
        Date expiration = Date.from(now.plus(1, ChronoUnit.HOURS));

        // You can add custom claims if needed
        Map<String, Object> claims = new HashMap<>();
        claims.put("scope", "sql_solution_submit"); // Example custom claim

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setAudience(audience)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        log.info("JWT token successfully generated.");
        return jwt;
    }
}
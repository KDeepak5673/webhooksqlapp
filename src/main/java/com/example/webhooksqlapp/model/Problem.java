package com.example.webhooksqlapp.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

// Lombok's @Data annotation automatically generates getters, setters,
// toString, equals, and hashCode methods.
@Data
public class Problem {
    @Id // Marks this field as the primary key
    private Long id;
    private String question;
    private String data; // Stores problem-specific data, e.g., in JSON format
}

// src/main/java/com/example/webhooksqlapp/model/Solution.java
package com.example.webhooksqlapp.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

@Data
public class class Solution {
    @Id
    private Long id;
    private Long problemId;
    private String solutionQuery;
    private LocalDateTime createdAt;
}
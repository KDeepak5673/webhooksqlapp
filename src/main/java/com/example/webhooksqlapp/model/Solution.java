package com.example.webhooksqlapp.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

@Data
public class Solution {
    @Id
    private Long id;
    private Long problemId;
    private String solutionQuery;
    private LocalDateTime createdAt;
}

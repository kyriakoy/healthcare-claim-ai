package com.example.healthcareclaims.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResult {
    private String ruleId;
    private String status; // VIOLATION or PASS
    private String message;
    private String aiInterpretationDetails;
} 
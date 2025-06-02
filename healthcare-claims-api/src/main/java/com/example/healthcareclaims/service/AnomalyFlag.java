package com.example.healthcareclaims.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnomalyFlag {
    private String type;
    private String message;
    private String details;
} 
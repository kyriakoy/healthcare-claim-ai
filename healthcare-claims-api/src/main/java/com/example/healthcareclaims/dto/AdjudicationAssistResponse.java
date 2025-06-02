package com.example.healthcareclaims.dto;

import com.example.healthcareclaims.service.ValidationResult;
import com.example.healthcareclaims.service.AnomalyFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdjudicationAssistResponse {
    private String claimId;
    private List<ValidationResult> validationResults;
    private String memberHistorySummary;
    private List<AnomalyFlag> anomalyFlags;
    private String suggestedAdjudicationNotes;
    private String suggestedAction;
} 
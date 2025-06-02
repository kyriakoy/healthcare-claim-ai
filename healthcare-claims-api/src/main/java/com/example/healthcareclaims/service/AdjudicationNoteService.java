package com.example.healthcareclaims.service;

import com.example.healthcareclaims.entity.Claim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdjudicationNoteService {
    private final AIPromptService aiPromptService;

    @Autowired
    public AdjudicationNoteService(AIPromptService aiPromptService) {
        this.aiPromptService = aiPromptService;
    }

    public String buildAdjudicationNotePrompt(Claim claim, List<ValidationResult> validationResults,
                                              String memberHistorySummary, List<AnomalyFlag> anomalyFlags) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a concise adjudication note for the following claim:\n\n");
        prompt.append("Claim ID: ").append(claim.getClaimId()).append("\n");
        prompt.append("Procedure Codes: ").append(claim.getSubmittedProcedureCodes()).append("\n");
        prompt.append("Diagnosis Codes: ").append(claim.getSubmittedDiagnosisCodes()).append("\n");
        prompt.append("Billed Amount: $").append(claim.getBilledAmount()).append("\n\n");

        prompt.append("Validation Results:\n");
        for (ValidationResult result : validationResults) {
            prompt.append("- Rule ").append(result.getRuleId())
                  .append(": ").append(result.getStatus())
                  .append(" - ").append(result.getMessage()).append("\n");
        }
        prompt.append("\n");

        prompt.append("Member History Summary:\n").append(memberHistorySummary).append("\n\n");

        prompt.append("Anomaly Flags:\n");
        for (AnomalyFlag flag : anomalyFlags) {
            prompt.append("- ").append(flag.getType())
                  .append(": ").append(flag.getMessage()).append("\n");
        }
        prompt.append("\n");

        prompt.append("Based on the above information, write a concise adjudication note (2-3 sentences) ");
        prompt.append("that summarizes the key findings and suggests an appropriate action (approve, deny, or pend for review).");

        return prompt.toString();
    }

    public String generateSuggestedNotes(Claim claim, List<ValidationResult> validationResults,
                                         String memberHistorySummary, List<AnomalyFlag> anomalyFlags) {
        String prompt = buildAdjudicationNotePrompt(claim, validationResults, memberHistorySummary, anomalyFlags);
        return aiPromptService.executePrompt(prompt);
    }

    public String suggestAction(List<ValidationResult> validationResults, List<AnomalyFlag> anomalyFlags) {
        boolean hasViolations = validationResults.stream()
            .anyMatch(r -> "VIOLATION".equals(r.getStatus()));
        boolean hasAnomalies = !anomalyFlags.isEmpty();
        if (hasViolations) {
            return "DENY";
        } else if (hasAnomalies) {
            return "PENDING_MANUAL_REVIEW";
        } else {
            return "APPROVE";
        }
    }
} 
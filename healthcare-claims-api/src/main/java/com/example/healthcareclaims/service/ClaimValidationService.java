package com.example.healthcareclaims.service;

import com.example.healthcareclaims.entity.Claim;
import com.example.healthcareclaims.entity.Member;
import com.example.healthcareclaims.entity.AdjudicationRule;
import com.example.healthcareclaims.repository.AdjudicationRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClaimValidationService {
    private final AdjudicationRuleRepository ruleRepository;
    private final AIPromptService aiPromptService;

    @Autowired
    public ClaimValidationService(AdjudicationRuleRepository ruleRepository, AIPromptService aiPromptService) {
        this.ruleRepository = ruleRepository;
        this.aiPromptService = aiPromptService;
    }

    public List<ValidationResult> validateClaim(Claim claim, Member member) {
        List<AdjudicationRule> applicableRules = findApplicableRules(claim);
        List<ValidationResult> results = new ArrayList<>();

        for (AdjudicationRule rule : applicableRules) {
            String prompt = aiPromptService.generateRuleValidationPrompt(claim, member, rule);
            String aiResponse = aiPromptService.executePrompt(prompt);
            ValidationResult result = parseValidationResponse(aiResponse, rule);
            results.add(result);
        }

        return results;
    }

    private List<AdjudicationRule> findApplicableRules(Claim claim) {
        // For MVP, return all rules
        return ruleRepository.findAll();
    }

    private ValidationResult parseValidationResponse(String aiResponse, AdjudicationRule rule) {
        ValidationResult result = new ValidationResult();
        result.setRuleId(rule.getRuleId());
        result.setAiInterpretationDetails(aiResponse);

        // Simple parsing logic - can be enhanced
        if (aiResponse.toUpperCase().contains("YES")) {
            result.setStatus("VIOLATION");
            // Extract explanation after "YES"
            int index = aiResponse.toUpperCase().indexOf("YES");
            if (index >= 0 && aiResponse.length() > index + 3) {
                result.setMessage(aiResponse.substring(index + 3).trim());
            } else {
                result.setMessage("Rule violation detected.");
            }
        } else {
            result.setStatus("PASS");
            // Extract explanation after "NO"
            int index = aiResponse.toUpperCase().indexOf("NO");
            if (index >= 0 && aiResponse.length() > index + 2) {
                result.setMessage(aiResponse.substring(index + 2).trim());
            } else {
                result.setMessage("No rule violation detected.");
            }
        }

        return result;
    }
} 
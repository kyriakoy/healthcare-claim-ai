package com.example.healthcareclaims.service;

import com.example.healthcareclaims.entity.Claim;
import com.example.healthcareclaims.entity.Member;
import com.example.healthcareclaims.entity.AdjudicationRule;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AIPromptService {
    private final ChatModel chatModel;

    public AIPromptService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String generateRuleValidationPrompt(Claim claim, Member member, AdjudicationRule rule) {
        return "Given the following claim and member information, determine if the claim violates the specified rule.\n\n" +
                "Claim Information:\n" +
                "- Claim ID: " + claim.getClaimId() + "\n" +
                "- Date of Service: " + claim.getDateOfService() + "\n" +
                "- Procedure Codes: " + claim.getSubmittedProcedureCodes() + "\n" +
                "- Diagnosis Codes: " + claim.getSubmittedDiagnosisCodes() + "\n" +
                "- Billed Amount: $" + claim.getBilledAmount() + "\n\n" +
                "Member Information:\n" +
                "- Member ID: " + member.getMemberId() + "\n" +
                "- Plan: " + member.getCurrentPlanId() + "\n" +
                "- Benefit Utilization: " + member.getBenefitUtilizationSummary() + "\n\n" +
                "Rule to Check:\n" +
                "- Rule ID: " + rule.getRuleId() + "\n" +
                "- Rule Name: " + rule.getRuleName() + "\n" +
                "- Rule Logic: " + rule.getConditionLogicText() + "\n\n" +
                "Does this claim violate the rule? Answer with YES or NO, followed by a brief explanation.";
    }

    public String executePrompt(String prompt) {
        Prompt aiPrompt = new Prompt(new UserMessage(prompt));
        ChatResponse response = chatModel.call(aiPrompt);
        return response.getResults().get(0).getOutput().getText();
    }

    public String generateMemberHistorySummaryPrompt(Member member, List<Claim> pastClaims) {
        StringBuilder claimDetails = new StringBuilder();
        for (Claim claim : pastClaims) {
            claimDetails.append("- Date: ").append(claim.getDateOfService())
                        .append(", Procedures: ").append(claim.getSubmittedProcedureCodes())
                        .append(", Diagnoses: ").append(claim.getSubmittedDiagnosisCodes())
                        .append(", Amount: $").append(claim.getBilledAmount())
                        .append("\n");
        }
        return "Summarize the following member's claim history and benefit utilization in 2-3 sentences, focusing on information relevant for adjudicating new claims.\n\n" +
                "Member Information:\n" +
                "- Member ID: " + member.getMemberId() + "\n" +
                "- Plan: " + member.getCurrentPlanId() + "\n" +
                "- Benefit Utilization: " + member.getBenefitUtilizationSummary() + "\n\n" +
                "Past Claims:\n" + claimDetails.toString();
    }
} 
package com.example.healthcareclaims.service;

import com.example.healthcareclaims.entity.Claim;
import com.example.healthcareclaims.entity.Member;
import com.example.healthcareclaims.repository.ClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MemberHistoryService {
    private final ClaimRepository claimRepository;
    private final AIPromptService aiPromptService;

    @Autowired
    public MemberHistoryService(ClaimRepository claimRepository, AIPromptService aiPromptService) {
        this.claimRepository = claimRepository;
        this.aiPromptService = aiPromptService;
    }

    public String generateMemberHistorySummary(Member member) {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        List<Claim> pastClaims = claimRepository.findByMemberIdAndDateOfServiceBetween(
                member.getMemberId(), oneYearAgo, LocalDate.now());

        if (pastClaims.isEmpty()) {
            return "Member " + member.getMemberId() + " has no claims in the past 12 months.";
        }

        String prompt = aiPromptService.generateMemberHistorySummaryPrompt(member, pastClaims);
        return aiPromptService.executePrompt(prompt);
    }
} 
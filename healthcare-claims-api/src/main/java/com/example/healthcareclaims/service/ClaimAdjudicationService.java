package com.example.healthcareclaims.service;

import com.example.healthcareclaims.dto.AdjudicationAssistRequest;
import com.example.healthcareclaims.dto.AdjudicationAssistResponse;
import com.example.healthcareclaims.entity.Claim;
import com.example.healthcareclaims.entity.Member;
import com.example.healthcareclaims.entity.ClaimProcessingLog;
import com.example.healthcareclaims.repository.ClaimRepository;
import com.example.healthcareclaims.repository.MemberRepository;
import com.example.healthcareclaims.repository.ClaimProcessingLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class ClaimAdjudicationService {
    private final ClaimRepository claimRepository;
    private final MemberRepository memberRepository;
    private final ClaimValidationService claimValidationService;
    private final MemberHistoryService memberHistoryService;
    private final AnomalyDetectionService anomalyDetectionService;
    private final AdjudicationNoteService adjudicationNoteService;
    private final ClaimProcessingLogRepository claimProcessingLogRepository;

    @Autowired
    public ClaimAdjudicationService(
            ClaimRepository claimRepository,
            MemberRepository memberRepository,
            ClaimValidationService claimValidationService,
            MemberHistoryService memberHistoryService,
            AnomalyDetectionService anomalyDetectionService,
            AdjudicationNoteService adjudicationNoteService,
            ClaimProcessingLogRepository claimProcessingLogRepository) {
        this.claimRepository = claimRepository;
        this.memberRepository = memberRepository;
        this.claimValidationService = claimValidationService;
        this.memberHistoryService = memberHistoryService;
        this.anomalyDetectionService = anomalyDetectionService;
        this.adjudicationNoteService = adjudicationNoteService;
        this.claimProcessingLogRepository = claimProcessingLogRepository;
    }

    public AdjudicationAssistResponse assistAdjudication(String claimId, StringBuilder outPrompt) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Claim not found"));
        Member member = memberRepository.findById(claim.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        List<ValidationResult> validationResults = claimValidationService.validateClaim(claim, member);
        String memberHistorySummary = memberHistoryService.generateMemberHistorySummary(member);
        List<AnomalyFlag> anomalyFlags = anomalyDetectionService.detectAnomalies(claim);
        String prompt = adjudicationNoteService.buildAdjudicationNotePrompt(claim, validationResults, memberHistorySummary, anomalyFlags);
        String suggestedNotes = adjudicationNoteService.generateSuggestedNotes(claim, validationResults, memberHistorySummary, anomalyFlags);
        String suggestedAction = adjudicationNoteService.suggestAction(validationResults, anomalyFlags);

        // Log to ClaimProcessingLog
        ClaimProcessingLog log = new ClaimProcessingLog();
        log.setClaimId(claimId);
        log.setTimestamp(LocalDateTime.now());
        log.setActor("MCP_AI_ASSISTANT");
        log.setActionOrFinding("ASSIST_ADJUDICATION");
        log.setNotes("Suggested Notes: " + suggestedNotes + "\nSuggested Action: " + suggestedAction);
        claimProcessingLogRepository.save(log);

        if (outPrompt != null) outPrompt.append(prompt);

        return new AdjudicationAssistResponse(
                claimId,
                validationResults,
                memberHistorySummary,
                anomalyFlags,
                suggestedNotes,
                suggestedAction
        );
    }
} 
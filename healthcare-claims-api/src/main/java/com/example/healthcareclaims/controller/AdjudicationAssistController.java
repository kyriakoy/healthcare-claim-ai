package com.example.healthcareclaims.controller;

import com.example.healthcareclaims.dto.AdjudicationAssistRequest;
import com.example.healthcareclaims.dto.AdjudicationAssistResponse;
import com.example.healthcareclaims.service.ClaimAdjudicationService;
import com.example.healthcareclaims.entity.Claim;
import com.example.healthcareclaims.entity.Member;
import com.example.healthcareclaims.entity.AdjudicationRule;
import com.example.healthcareclaims.entity.ClaimProcessingLog;
import com.example.healthcareclaims.repository.ClaimRepository;
import com.example.healthcareclaims.repository.MemberRepository;
import com.example.healthcareclaims.repository.AdjudicationRuleRepository;
import com.example.healthcareclaims.repository.ClaimProcessingLogRepository;
import com.example.healthcareclaims.service.AIPromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/mcp/claims")
public class AdjudicationAssistController {
    private final ClaimAdjudicationService claimAdjudicationService;
    private final ClaimRepository claimRepository;
    private final MemberRepository memberRepository;
    private final AdjudicationRuleRepository ruleRepository;
    private final ClaimProcessingLogRepository logRepository;
    private final AIPromptService aiPromptService;

    // In-memory map to store last LLM response per claim for demo
    private static final ConcurrentHashMap<String, String> llmResponseCache = new ConcurrentHashMap<>();

    // In-memory map to store last adjudication note prompt per claim for demo
    private static final ConcurrentHashMap<String, String> notePromptCache = new ConcurrentHashMap<>();

    @Autowired
    public AdjudicationAssistController(ClaimAdjudicationService claimAdjudicationService,
                                        ClaimRepository claimRepository,
                                        MemberRepository memberRepository,
                                        AdjudicationRuleRepository ruleRepository,
                                        ClaimProcessingLogRepository logRepository,
                                        AIPromptService aiPromptService) {
        this.claimAdjudicationService = claimAdjudicationService;
        this.claimRepository = claimRepository;
        this.memberRepository = memberRepository;
        this.ruleRepository = ruleRepository;
        this.logRepository = logRepository;
        this.aiPromptService = aiPromptService;
    }

    @PostMapping("/assist-adjudication")
    public ResponseEntity<AdjudicationAssistResponse> assistAdjudication(@RequestBody AdjudicationAssistRequest request) {
        StringBuilder promptBuilder = new StringBuilder();
        AdjudicationAssistResponse response = claimAdjudicationService.assistAdjudication(request.getClaimId(), promptBuilder);
        llmResponseCache.put(request.getClaimId(), response.getSuggestedAdjudicationNotes());
        notePromptCache.put(request.getClaimId(), promptBuilder.toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{claimId}/details")
    public Map<String, Object> getClaimDetails(@PathVariable String claimId) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Claim not found"));
        Member member = memberRepository.findById(claim.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
        List<AdjudicationRule> rules = ruleRepository.findAll();
        return Map.of("claim", claim, "member", member, "rules", rules);
    }

    @GetMapping("/{claimId}/prompt")
    public Map<String, String> getPrompt(@PathVariable String claimId) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Claim not found"));
        Member member = memberRepository.findById(claim.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
        // For demo, use the first rule
        AdjudicationRule rule = ruleRepository.findAll().stream().findFirst().orElse(null);
        String prompt = rule != null ? aiPromptService.generateRuleValidationPrompt(claim, member, rule) : "No rule available.";
        return Map.of("prompt", prompt);
    }

    @GetMapping("/{claimId}/llm-response")
    public Map<String, String> getLLMResponse(@PathVariable String claimId) {
        String llmResponse = llmResponseCache.get(claimId);
        if (llmResponse == null) {
            return Map.of("llmResponse", "No LLM response found for this claim. Please process the claim first.");
        }
        return Map.of("llmResponse", llmResponse);
    }

    @GetMapping("/{claimId}/logs")
    public List<ClaimProcessingLog> getLogs(@PathVariable String claimId) {
        return logRepository.findByClaimIdOrderByTimestampDesc(claimId);
    }

    @GetMapping("/{claimId}/adjudication-note-prompt")
    public Map<String, String> getAdjudicationNotePrompt(@PathVariable String claimId) {
        String prompt = notePromptCache.get(claimId);
        if (prompt == null) {
            return Map.of("adjudicationNotePrompt", "No adjudication note prompt found for this claim. Please process the claim first.");
        }
        return Map.of("adjudicationNotePrompt", prompt);
    }
} 
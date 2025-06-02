package com.example.healthcareclaims.service;

import com.example.healthcareclaims.entity.Claim;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdjudicationNoteServiceTest {
    @Mock
    private AIPromptService aiPromptService;
    @InjectMocks
    private AdjudicationNoteService adjudicationNoteService;

    private Claim claim;
    private String memberHistorySummary;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        claim = new Claim("CLM1", "MEM1", "PROV1", "PLAN1", LocalDate.now(),
                "[\"CPT:1234\"]", "[\"ICD10:A00\"]", new BigDecimal("500.00"), LocalDate.now(),
                "RECEIVED", null, null, null);
        memberHistorySummary = "Member summary.";
    }

    @Test
    void testSuggestAction_Deny() {
        ValidationResult violation = new ValidationResult("R1", "VIOLATION", "msg", "details");
        String action = adjudicationNoteService.suggestAction(List.of(violation), Collections.emptyList());
        assertEquals("DENY", action);
    }

    @Test
    void testSuggestAction_PendingManualReview() {
        ValidationResult pass = new ValidationResult("R1", "PASS", "msg", "details");
        AnomalyFlag anomaly = new AnomalyFlag("FREQUENT_CLAIMS", "msg", "details");
        String action = adjudicationNoteService.suggestAction(List.of(pass), List.of(anomaly));
        assertEquals("PENDING_MANUAL_REVIEW", action);
    }

    @Test
    void testSuggestAction_Approve() {
        ValidationResult pass = new ValidationResult("R1", "PASS", "msg", "details");
        String action = adjudicationNoteService.suggestAction(List.of(pass), Collections.emptyList());
        assertEquals("APPROVE", action);
    }

    @Test
    void testGenerateSuggestedNotes() {
        ValidationResult pass = new ValidationResult("R1", "PASS", "msg", "details");
        AnomalyFlag anomaly = new AnomalyFlag("FREQUENT_CLAIMS", "msg", "details");
        when(aiPromptService.executePrompt(anyString())).thenReturn("AI NOTE");
        String note = adjudicationNoteService.generateSuggestedNotes(claim, List.of(pass), memberHistorySummary, List.of(anomaly));
        assertEquals("AI NOTE", note);
    }
} 
package com.example.healthcareclaims.service;

import com.example.healthcareclaims.entity.Claim;
import com.example.healthcareclaims.entity.Member;
import com.example.healthcareclaims.repository.ClaimRepository;
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

class MemberHistoryServiceTest {
    @Mock
    private ClaimRepository claimRepository;
    @Mock
    private AIPromptService aiPromptService;
    @InjectMocks
    private MemberHistoryService memberHistoryService;

    private Member member;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        member = new Member("MEM001", "PLAN_GOLD", LocalDate.of(1980, 5, 15), "M", "{\"utilization\": 5}");
    }

    @Test
    void testGenerateMemberHistorySummary_NoClaims() {
        when(claimRepository.findByMemberIdAndDateOfServiceBetween(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());
        String result = memberHistoryService.generateMemberHistorySummary(member);
        assertTrue(result.contains("has no claims in the past 12 months"));
    }

    @Test
    void testGenerateMemberHistorySummary_WithClaims() {
        Claim claim = new Claim("CLM1001", "MEM001", "PROV00A", "PLAN_GOLD", LocalDate.now().minusDays(5),
                "[\"CPT:99214\"]", "[\"ICD10:M54.5\"]", new BigDecimal("150.00"), LocalDate.now().minusDays(3),
                "RECEIVED", null, null, null);
        List<Claim> claims = List.of(claim);
        when(claimRepository.findByMemberIdAndDateOfServiceBetween(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(claims);
        when(aiPromptService.generateMemberHistorySummaryPrompt(eq(member), eq(claims))).thenReturn("PROMPT");
        when(aiPromptService.executePrompt("PROMPT")).thenReturn("AI SUMMARY");
        String result = memberHistoryService.generateMemberHistorySummary(member);
        assertEquals("AI SUMMARY", result);
    }
} 
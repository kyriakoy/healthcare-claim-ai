package com.example.healthcareclaims.service;

import com.example.healthcareclaims.entity.Claim;
import com.example.healthcareclaims.repository.ClaimRepository;
import com.example.healthcareclaims.repository.ProviderRepository;
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

class AnomalyDetectionServiceTest {
    @Mock
    private ClaimRepository claimRepository;
    @Mock
    private ProviderRepository providerRepository;
    @InjectMocks
    private AnomalyDetectionService anomalyDetectionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDetectAnomalies_HighBilling() {
        Claim claim = new Claim("CLM1", "MEM1", "PROV1", "PLAN1", LocalDate.now(),
                "[\"CPT:1234\"]", "[\"ICD10:A00\"]", new BigDecimal("1500.00"), LocalDate.now(),
                "RECEIVED", null, null, null);
        when(claimRepository.findByMemberIdAndDateOfServiceBetween(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());
        List<AnomalyFlag> anomalies = anomalyDetectionService.detectAnomalies(claim);
        assertTrue(anomalies.stream().anyMatch(a -> "BILLING_HIGH".equals(a.getType())));
    }

    @Test
    void testDetectAnomalies_FrequentClaims() {
        Claim baseClaim = new Claim("CLM1", "MEM1", "PROV1", "PLAN1", LocalDate.now(),
                "[\"CPT:1234\"]", "[\"ICD10:A00\"]", new BigDecimal("500.00"), LocalDate.now(),
                "RECEIVED", null, null, null);
        Claim similar1 = new Claim("CLM2", "MEM1", "PROV1", "PLAN1", LocalDate.now().minusDays(10),
                "[\"CPT:1234\"]", "[\"ICD10:A00\"]", new BigDecimal("400.00"), LocalDate.now().minusDays(10),
                "RECEIVED", null, null, null);
        Claim similar2 = new Claim("CLM3", "MEM1", "PROV1", "PLAN1", LocalDate.now().minusDays(20),
                "[\"CPT:1234\"]", "[\"ICD10:A00\"]", new BigDecimal("300.00"), LocalDate.now().minusDays(20),
                "RECEIVED", null, null, null);
        List<Claim> recentClaims = List.of(baseClaim, similar1, similar2);
        when(claimRepository.findByMemberIdAndDateOfServiceBetween(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(recentClaims);
        List<AnomalyFlag> anomalies = anomalyDetectionService.detectAnomalies(baseClaim);
        assertTrue(anomalies.stream().anyMatch(a -> "FREQUENT_CLAIMS".equals(a.getType())));
    }

    @Test
    void testDetectAnomalies_NoAnomalies() {
        Claim claim = new Claim("CLM1", "MEM1", "PROV1", "PLAN1", LocalDate.now(),
                "[\"CPT:1234\"]", "[\"ICD10:A00\"]", new BigDecimal("500.00"), LocalDate.now(),
                "RECEIVED", null, null, null);
        when(claimRepository.findByMemberIdAndDateOfServiceBetween(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());
        List<AnomalyFlag> anomalies = anomalyDetectionService.detectAnomalies(claim);
        assertTrue(anomalies.isEmpty());
    }
} 
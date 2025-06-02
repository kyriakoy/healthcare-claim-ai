package com.example.healthcareclaims.controller;

import com.example.healthcareclaims.entity.Claim;
import com.example.healthcareclaims.entity.Member;
import com.example.healthcareclaims.repository.ClaimRepository;
import com.example.healthcareclaims.repository.MemberRepository;
import com.example.healthcareclaims.service.ClaimValidationService;
import com.example.healthcareclaims.service.ValidationResult;
import com.example.healthcareclaims.service.AnomalyDetectionService;
import com.example.healthcareclaims.service.AnomalyFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimValidationController {
    private final ClaimRepository claimRepository;
    private final MemberRepository memberRepository;
    private final ClaimValidationService validationService;
    private final AnomalyDetectionService anomalyDetectionService;

    @Autowired
    public ClaimValidationController(ClaimRepository claimRepository, MemberRepository memberRepository, ClaimValidationService validationService, AnomalyDetectionService anomalyDetectionService) {
        this.claimRepository = claimRepository;
        this.memberRepository = memberRepository;
        this.validationService = validationService;
        this.anomalyDetectionService = anomalyDetectionService;
    }

    @GetMapping("/validate/{claimId}")
    public ResponseEntity<List<ValidationResult>> validateClaim(@PathVariable String claimId) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Claim not found"));
        Member member = memberRepository.findById(claim.getMemberId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
        List<ValidationResult> results = validationService.validateClaim(claim, member);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/anomalies/{claimId}")
    public ResponseEntity<List<AnomalyFlag>> getAnomalies(@PathVariable String claimId) {
        Claim claim = claimRepository.findById(claimId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Claim not found"));
        List<AnomalyFlag> anomalies = anomalyDetectionService.detectAnomalies(claim);
        return ResponseEntity.ok(anomalies);
    }
} 
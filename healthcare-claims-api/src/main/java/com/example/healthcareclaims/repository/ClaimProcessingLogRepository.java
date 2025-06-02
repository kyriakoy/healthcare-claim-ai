package com.example.healthcareclaims.repository;

import com.example.healthcareclaims.entity.ClaimProcessingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimProcessingLogRepository extends JpaRepository<ClaimProcessingLog, Long> {
    List<ClaimProcessingLog> findByClaimIdOrderByTimestampDesc(String claimId);
} 
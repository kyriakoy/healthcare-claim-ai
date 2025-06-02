package com.example.healthcareclaims.repository;

import com.example.healthcareclaims.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, String> {
    List<Claim> findByMemberId(String memberId);
    List<Claim> findByMemberIdAndDateOfServiceBetween(String memberId, LocalDate startDate, LocalDate endDate);
} 
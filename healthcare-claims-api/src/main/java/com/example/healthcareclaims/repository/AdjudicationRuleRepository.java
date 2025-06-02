package com.example.healthcareclaims.repository;

import com.example.healthcareclaims.entity.AdjudicationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdjudicationRuleRepository extends JpaRepository<AdjudicationRule, String> {
    List<AdjudicationRule> findByAppliesToProcedureCodesContaining(String procedureCode);
    List<AdjudicationRule> findByAppliesToDiagnosisCodesContaining(String diagnosisCode);
} 
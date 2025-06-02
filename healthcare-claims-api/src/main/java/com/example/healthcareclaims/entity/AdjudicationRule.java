package com.example.healthcareclaims.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "adjudication_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdjudicationRule {
    @Id
    private String ruleId;
    private String ruleName;
    private String description;
    private String appliesToProcedureCodes; // JSON array or null
    private String appliesToDiagnosisCodes; // JSON array or null
    @Column(length = 2000)
    private String conditionLogicText;
    private String severity; // WARNING, ERROR, INFO
} 
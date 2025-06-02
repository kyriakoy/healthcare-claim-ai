package com.example.healthcareclaims.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "claims")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Claim {
    @Id
    private String claimId;
    private String memberId;
    private String providerId;
    private String planIdAtSubmission;
    private LocalDate dateOfService;
    private String submittedProcedureCodes; // JSON array
    private String submittedDiagnosisCodes; // JSON array
    private BigDecimal billedAmount;
    private LocalDate submissionDate;
    private String status; // RECEIVED, PROCESSING, ADJUDICATED
    private BigDecimal adjudicatedAmount;
    private LocalDate adjudicationDate;
    private String adjudicatorNotes;
} 
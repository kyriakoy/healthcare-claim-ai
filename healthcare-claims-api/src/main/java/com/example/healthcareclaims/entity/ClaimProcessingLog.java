package com.example.healthcareclaims.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "claim_processing_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimProcessingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;
    private String claimId;
    private LocalDateTime timestamp;
    private String actor;
    private String actionOrFinding;
    @Column(length = 2000)
    private String notes;
} 
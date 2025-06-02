package com.example.healthcareclaims.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "providers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Provider {
    @Id
    private String providerId;
    private String providerName;
    private String specialtyTags; // JSON array or comma-separated
    private String region;
} 
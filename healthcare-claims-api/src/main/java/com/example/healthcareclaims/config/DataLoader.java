package com.example.healthcareclaims.config;

import com.example.healthcareclaims.entity.*;
import com.example.healthcareclaims.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Test Data Scenarios (Loaded at Startup):
 *
 * Members:
 *   - MEM001: PLAN_GOLD, M, 1980-05-15, moderate utilization
 *   - MEM002: PLAN_SILVER, F, 1992-11-02, low utilization
 *   - MEM003: PLAN_BRONZE, M, 1975-03-22, high utilization
 *
 * Providers:
 *   - PROV00A: ABC Medical Center, General Practice/Physiotherapy, Northeast
 *   - PROV00B: XYZ Ortho Clinic, Orthopedics, Midwest
 *   - PROV00C: QuickCare, Urgent Care, South
 *
 * Adjudication Rules:
 *   - R101: Pre-authorization for Non-Emergency Specialist Visits (ERROR)
 *   - R102: Annual physiotherapy visit limit (WARNING)
 *   - R103: High-cost procedure requires review (ERROR)
 *
 * Claims:
 *   - CLM1001: MEM001, PROV00A, CPT:99214, ICD10:M54.5, $150, triggers R101 (violation)
 *   - CLM2001: MEM001, PROV00A, CPT:99214, ICD10:M54.5, $1500, triggers high billing anomaly
 *   - CLM2002/2003: MEM001, PROV00A, CPT:99214, ICD10:M54.5, $200/$180, frequent claims anomaly
 *   - CLM3001: MEM002, PROV00B, CPT:97110, ICD10:S83.2, $120, should PASS all rules
 *   - CLM3002: MEM002, PROV00B, CPT:97110, ICD10:S83.2, $1200, triggers R103 (violation)
 *   - CLM4001: MEM003, PROV00C, CPT:99203, ICD10:J06.9, $90, urgent care, should PASS
 *   - CLM4002: MEM003, PROV00C, CPT:97110, ICD10:M54.5, $100, exceeds annual physio limit, triggers R102 (warning)
 *
 * Use these for API and UI testing. See README for sample requests.
 */
@Component
@Profile("!test")
public class DataLoader implements CommandLineRunner {
    private final ClaimRepository claimRepository;
    private final MemberRepository memberRepository;
    private final ProviderRepository providerRepository;
    private final AdjudicationRuleRepository ruleRepository;

    @Autowired
    public DataLoader(ClaimRepository claimRepository, MemberRepository memberRepository,
                     ProviderRepository providerRepository, AdjudicationRuleRepository ruleRepository) {
        this.claimRepository = claimRepository;
        this.memberRepository = memberRepository;
        this.providerRepository = providerRepository;
        this.ruleRepository = ruleRepository;
    }

    @Override
    public void run(String... args) {
        // Load sample members
        Member member1 = new Member("MEM001", "PLAN_GOLD", LocalDate.of(1980, 5, 15), "M",
                "{\"physiotherapy_visits_used\": 2, \"physiotherapy_annual_limit\": 20, \"deductible_met_usd\": 500, \"deductible_total_usd\": 1000}");
        Member member2 = new Member("MEM002", "PLAN_SILVER", LocalDate.of(1992, 11, 2), "F",
                "{\"physiotherapy_visits_used\": 0, \"physiotherapy_annual_limit\": 10, \"deductible_met_usd\": 200, \"deductible_total_usd\": 2000}");
        Member member3 = new Member("MEM003", "PLAN_BRONZE", LocalDate.of(1975, 3, 22), "M",
                "{\"physiotherapy_visits_used\": 12, \"physiotherapy_annual_limit\": 12, \"deductible_met_usd\": 1200, \"deductible_total_usd\": 1200}");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        // Load sample providers
        Provider provider1 = new Provider("PROV00A", "ABC Medical Center", "[\"General Practice\", \"Physiotherapy\"]", "Northeast");
        Provider provider2 = new Provider("PROV00B", "XYZ Ortho Clinic", "[\"Orthopedics\"]", "Midwest");
        Provider provider3 = new Provider("PROV00C", "QuickCare", "[\"Urgent Care\"]", "South");
        providerRepository.save(provider1);
        providerRepository.save(provider2);
        providerRepository.save(provider3);

        // Load sample adjudication rules
        AdjudicationRule rule1 = new AdjudicationRule("R101", "Pre-authorization for Non-Emergency Specialist Visits",
                "Ensures specialist visits that are not emergencies have prior authorization.",
                null, null, "IF service_type IS 'Specialist Visit' AND emergency_indicator IS FALSE AND member_preauthorization_list DOES NOT CONTAIN service_code THEN VIOLATION. ELSE PASS.", "ERROR");
        AdjudicationRule rule2 = new AdjudicationRule("R102", "Annual physiotherapy visit limit",
                "Warn if member exceeds annual physiotherapy visit limit.",
                "[\"CPT:97110\"]", null, "IF procedure_code IS 'CPT:97110' AND physio_visits_this_year >= physio_annual_limit THEN WARNING. ELSE PASS.", "WARNING");
        AdjudicationRule rule3 = new AdjudicationRule("R103", "High-cost procedure requires review",
                "Flag if billed amount for any procedure exceeds $1000.",
                null, null, "IF billed_amount > 1000 THEN VIOLATION. ELSE PASS.", "ERROR");
        ruleRepository.save(rule1);
        ruleRepository.save(rule2);
        ruleRepository.save(rule3);

        // Claims for MEM001 (PLAN_GOLD)
        Claim claim1 = new Claim("CLM1001", "MEM001", "PROV00A", "PLAN_GOLD", LocalDate.now().minusDays(5),
                "[\"CPT:99214\"]", "[\"ICD10:M54.5\"]", new BigDecimal("150.00"), LocalDate.now().minusDays(3),
                "RECEIVED", null, null, null);
        Claim highBillingClaim = new Claim("CLM2001", "MEM001", "PROV00A", "PLAN_GOLD", LocalDate.now().minusDays(2),
                "[\"CPT:99214\"]", "[\"ICD10:M54.5\"]", new BigDecimal("1500.00"), LocalDate.now().minusDays(1),
                "RECEIVED", null, null, null);
        Claim similarClaim1 = new Claim("CLM2002", "MEM001", "PROV00A", "PLAN_GOLD", LocalDate.now().minusDays(10),
                "[\"CPT:99214\"]", "[\"ICD10:M54.5\"]", new BigDecimal("200.00"), LocalDate.now().minusDays(9),
                "RECEIVED", null, null, null);
        Claim similarClaim2 = new Claim("CLM2003", "MEM001", "PROV00A", "PLAN_GOLD", LocalDate.now().minusDays(20),
                "[\"CPT:99214\"]", "[\"ICD10:M54.5\"]", new BigDecimal("180.00"), LocalDate.now().minusDays(19),
                "RECEIVED", null, null, null);
        claimRepository.save(claim1);
        claimRepository.save(highBillingClaim);
        claimRepository.save(similarClaim1);
        claimRepository.save(similarClaim2);

        // Claims for MEM002 (PLAN_SILVER)
        Claim claim3 = new Claim("CLM3001", "MEM002", "PROV00B", "PLAN_SILVER", LocalDate.now().minusDays(7),
                "[\"CPT:97110\"]", "[\"ICD10:S83.2\"]", new BigDecimal("120.00"), LocalDate.now().minusDays(6),
                "RECEIVED", null, null, null);
        Claim claim4 = new Claim("CLM3002", "MEM002", "PROV00B", "PLAN_SILVER", LocalDate.now().minusDays(3),
                "[\"CPT:97110\"]", "[\"ICD10:S83.2\"]", new BigDecimal("1200.00"), LocalDate.now().minusDays(2),
                "RECEIVED", null, null, null);
        claimRepository.save(claim3);
        claimRepository.save(claim4);

        // Claims for MEM003 (PLAN_BRONZE)
        Claim claim5 = new Claim("CLM4001", "MEM003", "PROV00C", "PLAN_BRONZE", LocalDate.now().minusDays(4),
                "[\"CPT:99203\"]", "[\"ICD10:J06.9\"]", new BigDecimal("90.00"), LocalDate.now().minusDays(3),
                "RECEIVED", null, null, null);
        Claim claim6 = new Claim("CLM4002", "MEM003", "PROV00C", "PLAN_BRONZE", LocalDate.now().minusDays(1),
                "[\"CPT:97110\"]", "[\"ICD10:M54.5\"]", new BigDecimal("100.00"), LocalDate.now(),
                "RECEIVED", null, null, null);
        claimRepository.save(claim5);
        claimRepository.save(claim6);
    }
} 
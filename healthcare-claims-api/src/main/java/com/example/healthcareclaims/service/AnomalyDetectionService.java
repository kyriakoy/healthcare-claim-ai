package com.example.healthcareclaims.service;

import com.example.healthcareclaims.entity.Claim;
import com.example.healthcareclaims.repository.ClaimRepository;
import com.example.healthcareclaims.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnomalyDetectionService {
    private final ClaimRepository claimRepository;
    private final ProviderRepository providerRepository;

    @Autowired
    public AnomalyDetectionService(ClaimRepository claimRepository, ProviderRepository providerRepository) {
        this.claimRepository = claimRepository;
        this.providerRepository = providerRepository;
    }

    public List<AnomalyFlag> detectAnomalies(Claim claim) {
        List<AnomalyFlag> anomalies = new ArrayList<>();
        checkForHighBillingAmount(claim, anomalies);
        checkForFrequentClaims(claim, anomalies);
        return anomalies;
    }

    private void checkForHighBillingAmount(Claim claim, List<AnomalyFlag> anomalies) {
        if (claim.getBilledAmount() != null && claim.getBilledAmount().compareTo(new BigDecimal("1000")) > 0) {
            AnomalyFlag flag = new AnomalyFlag();
            flag.setType("BILLING_HIGH");
            flag.setMessage("Billed amount $" + claim.getBilledAmount() + " exceeds $1000 threshold.");
            flag.setDetails("High billing amounts may require additional review for medical necessity and pricing.");
            anomalies.add(flag);
        }
    }

    private void checkForFrequentClaims(Claim claim, List<AnomalyFlag> anomalies) {
        LocalDate thirtyDaysAgo = claim.getDateOfService().minusDays(30);
        List<Claim> recentClaims = claimRepository.findByMemberIdAndDateOfServiceBetween(
                claim.getMemberId(), thirtyDaysAgo, claim.getDateOfService());
        long similarClaimsCount = recentClaims.stream()
                .filter(c -> !c.getClaimId().equals(claim.getClaimId()))
                .filter(c -> hasOverlappingProcedureCodes(c.getSubmittedProcedureCodes(), claim.getSubmittedProcedureCodes()))
                .count();
        if (similarClaimsCount >= 2) {
            AnomalyFlag flag = new AnomalyFlag();
            flag.setType("FREQUENT_CLAIMS");
            flag.setMessage("Member has " + similarClaimsCount + " similar claims in the last 30 days.");
            flag.setDetails("Frequent claims for similar services may indicate duplicate billing or medical necessity concerns.");
            anomalies.add(flag);
        }
    }

    private boolean hasOverlappingProcedureCodes(String codes1, String codes2) {
        // Simple implementation - in a real system, would parse JSON and compare properly
        return codes1 != null && codes1.equals(codes2);
    }
} 
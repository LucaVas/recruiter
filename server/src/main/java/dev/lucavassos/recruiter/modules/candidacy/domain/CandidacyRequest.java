package dev.lucavassos.recruiter.modules.candidacy.domain;

import java.time.LocalDateTime;

public record CandidacyRequest(
        Long recruiterId,
        Long jobId,
        Long candidateId,
        double relevantExperience,
        double expectedCtc,
        double officialNoticePeriod,
        double actualNoticePeriod,
        String reasonForQuickJoin,
        String remarks,
        String comments,
        LocalDateTime cvRatePaymentDate,
        LocalDateTime closureBonusPaymentDate
) {
}

package dev.lucavassos.recruiter.modules.candidacy.domain;

public record NewCandidacyRequest(
        Long jobId,
        String candidatePan,
        double relevantExperience,
        double expectedCtc,
        double officialNoticePeriod,
        double actualNoticePeriod,
        String reasonForQuickJoin,
        String remarks
) {
}

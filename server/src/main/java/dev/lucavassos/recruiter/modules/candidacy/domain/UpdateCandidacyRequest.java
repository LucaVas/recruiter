package dev.lucavassos.recruiter.modules.candidacy.domain;

public record UpdateCandidacyRequest(
        Long id,
        Double relevantExperience,
        Double expectedCtc,
        Double officialNoticePeriod,
        Double actualNoticePeriod,
        String reasonForQuickJoin,
        String remarks
) {
}

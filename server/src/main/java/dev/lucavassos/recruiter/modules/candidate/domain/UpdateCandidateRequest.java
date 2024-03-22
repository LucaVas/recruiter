package dev.lucavassos.recruiter.modules.candidate.domain;


import lombok.Getter;

@Getter
public class UpdateCandidateRequest extends NewCandidateRequest {

    private final Long id;
    private final CandidateStatus status;

    UpdateCandidateRequest(Long id, String name, String phone, String email, Double totalExperience, Double relevantExperience, String education, Double currentCtC, Double expectedCtC, Double officialNoticePeriod, Double actualNoticePeriod, String reasonForQuickJoin, String remarks, Long recruiterId, String pan, String comments, CandidateStatus status) {
        super(name, phone, email, totalExperience, relevantExperience, education, currentCtC, expectedCtC, officialNoticePeriod, actualNoticePeriod, reasonForQuickJoin, remarks, recruiterId, pan, comments);
        this.id = id;
        this.status = status;
    }
}

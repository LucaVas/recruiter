package dev.lucavassos.recruiter.modules.candidate.domain;

import lombok.*;

@Builder
@Getter
public class NewCandidateRequest  {

    private String name;
    private String phone;
    private String email;
    private Double totalExperience;
    private Double relevantExperience;
    private String education;
    private Double currentCtC;
    private Double expectedCtC;
    private Double officialNoticePeriod;
    private Double actualNoticePeriod;
    private String reasonForQuickJoin;
    private String remarks;
    private Long recruiterId;
    private String pan;
    private String comments;
}

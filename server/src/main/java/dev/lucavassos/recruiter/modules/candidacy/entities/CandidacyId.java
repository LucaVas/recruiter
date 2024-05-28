package dev.lucavassos.recruiter.modules.candidacy.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Embeddable
public class CandidacyId implements Serializable {
    private String candidatePan;
    private Long jobId;
}

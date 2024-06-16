package dev.lucavassos.recruiter.modules.candidacy.entities;

import dev.lucavassos.recruiter.modules.candidacy.domain.CandidacyStatus;
import dev.lucavassos.recruiter.modules.candidate.entities.Candidate;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.user.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@Table(name = "candidacies")
public class Candidacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "relevant_experience")
    @Min(0)
    private double relevantExperience;

    @Column(nullable = false, name = "expected_ctc")
    @Min(0)
    private double expectedCtc;

    @Column(nullable = false, name = "official_notice_period")
    @Min(0)
    private double officialNoticePeriod;

    @Column(nullable = false, name = "actual_notice_period")
    @Min(0)
    private double actualNoticePeriod;

    @Column(name = "reason_for_quick_join")
    private String reasonForQuickJoin;

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private CandidacyStatus status;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // relationships

    @OneToMany(
            mappedBy = "candidacy",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<CandidacyComment> comments = new HashSet<>();

    @ManyToOne
    @ToString.Exclude
    private Job job;

    @ManyToOne
    @ToString.Exclude
    private Candidate candidate;

    @ManyToOne
    @ToString.Exclude
    private User recruiter;
}

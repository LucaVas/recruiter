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
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(
            fetch = FetchType.LAZY // EAGER is by default
    )
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    // unidirectional
    @ManyToOne(
            fetch = FetchType.LAZY // EAGER is by default
    )
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    // unidirectional
    @ManyToOne(
            fetch = FetchType.LAZY // EAGER is by default
    )
    @JoinColumn(name = "recruiter_id")
    private User recruiter;

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

    // unidirectional
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "candidacy_id")
    private List<CandidacyComment> comments = new ArrayList<>();

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private CandidacyStatus status;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

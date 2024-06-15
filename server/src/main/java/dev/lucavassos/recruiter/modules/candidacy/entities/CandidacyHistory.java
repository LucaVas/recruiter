package dev.lucavassos.recruiter.modules.candidacy.entities;

import dev.lucavassos.recruiter.modules.candidacy.domain.CandidacyStatus;
import dev.lucavassos.recruiter.modules.user.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@Table(name = "candidacy_history")
public class CandidacyHistory {

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

    // unidirectional
    @ManyToOne(
            fetch = FetchType.LAZY // EAGER is by default
    )
    @JoinColumn(name = "candidacy_id", nullable = false)
    private Candidacy candidacy;

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private CandidacyStatus status;

    // relation to user who modified the candidacy
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User modifiedBy;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

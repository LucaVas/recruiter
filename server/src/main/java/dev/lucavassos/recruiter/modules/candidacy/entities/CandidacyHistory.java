package dev.lucavassos.recruiter.modules.candidacy.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="candidacy_history")
public class CandidacyHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "job_id", referencedColumnName = "job_id"),
            @JoinColumn(name = "candidate_pan", referencedColumnName = "candidate_pan")
    })
    private Candidacy candidacy;

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

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "comments")
    private String comments;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @CreationTimestamp
    @Column(updatable = false, name = "created_dtime")
    private LocalDateTime createdDTime;

    @UpdateTimestamp
    @Column(name = "modified_dtime")
    private LocalDateTime modifiedDTime;
}

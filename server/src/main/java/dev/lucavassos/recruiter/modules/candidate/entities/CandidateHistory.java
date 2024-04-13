package dev.lucavassos.recruiter.modules.candidate.entities;

import dev.lucavassos.recruiter.modules.candidate.domain.CandidateStatus;
import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.job.entities.Job;
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
@Table(name="candidates_history")
public class CandidateHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, unique = true, length = 10, name = "phone")
    private String phone;

    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @Column(nullable = false, name = "total_experience")
    @Min(0)
    private double totalExperience;

    @Column(nullable = false, name = "education")
    private String education;

    @Column(nullable = false, name = "current_ctc")
    @Min(0)
    private double currentCtc;

    @Column(nullable = false, name = "candidate_status")
    @Enumerated(EnumType.STRING)
    private CandidateStatus status;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @CreationTimestamp
    private LocalDateTime createdAt;
}

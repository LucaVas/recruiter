package dev.lucavassos.recruiter.modules.candidate.entities;

import dev.lucavassos.recruiter.modules.candidate.domain.CandidateStatus;
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
@Table(name = "candidates_history")
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

    // relation to candidate
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "candidate_pan")
    private Candidate candidate;

    // relation to user who modified the candidate
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User modifiedBy;

    @CreationTimestamp
    @Column(updatable = false, name = "created_dtime")
    private LocalDateTime createdDTime;

    @UpdateTimestamp
    @Column(name = "modified_dtime")
    private LocalDateTime modifiedDTime;
}

package dev.lucavassos.recruiter.modules.candidate.entities;

import dev.lucavassos.recruiter.modules.candidate.domain.CandidateStatus;
import dev.lucavassos.recruiter.modules.user.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
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
@Table(name = "candidates")
public class Candidate {

    @Id
    @Column(length = 10, name = "pan")
    @Size(min = 10, max = 10)
    private String pan;

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

    @Column(name = "candidate_status")
    @Enumerated(EnumType.STRING)
    private CandidateStatus status;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recuiter_id")
    private User recruiter;
}

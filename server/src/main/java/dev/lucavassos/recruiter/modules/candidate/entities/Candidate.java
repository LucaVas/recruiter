package dev.lucavassos.recruiter.modules.candidate.entities;

import dev.lucavassos.recruiter.modules.candidate.domain.CandidateStatus;
import dev.lucavassos.recruiter.modules.user.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
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
@Table(name="candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 10)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Min(0)
    private double totalExperience;

    @Column(nullable = false)
    @Min(0)
    private double relevantExperience;

    @Column(nullable = false)
    private String education;

    @Column
    @Min(0)
    private double currentCtc;

    @Column(nullable = false)
    @Min(0)
    private double expectedCtc;

    @Column(nullable = false)
    @Min(0)
    private double officialNoticePeriod;

    @Column(nullable = false)
    @Min(0)
    private double actualNoticePeriod;

    @Column
    private String reasonForQuickJoin;

    @Column
    private String remarks;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recuiter_id")
    private User recruiter;

    @Column(nullable = false, unique = true, length = 10)
    @Size(min = 10, max = 10)
    private String pan;

    @Column
    private String comments;

    @Column
    private CandidateStatus status = CandidateStatus.ACTIVE;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    public Candidate(String name, String phone, String email, double totalExperience, double relevantExperience, String education, double currentCtc, double expectedCtc, double officialNoticePeriod, double actualNoticePeriod, String reasonForQuickJoin, String remarks, User recruiter, String pan, String comments) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.totalExperience = totalExperience;
        this.relevantExperience = relevantExperience;
        this.education = education;
        this.currentCtc = currentCtc;
        this.expectedCtc = expectedCtc;
        this.officialNoticePeriod = officialNoticePeriod;
        this.actualNoticePeriod = actualNoticePeriod;
        this.reasonForQuickJoin = reasonForQuickJoin;
        this.remarks = remarks;
        this.recruiter = recruiter;
        this.pan = pan;
        this.comments = comments;
    }
}

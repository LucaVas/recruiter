package dev.lucavassos.recruiter.modules.job.entities;

import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.skills.Skill;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String client;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private JobStatus status = JobStatus.OPEN;

    @Column(nullable = false)
    @Min(0)
    private Integer wantedCVs;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;

    @Column(nullable = false)
    private String experienceRange;

    @Column(nullable = false)
    @Min(0)
    private Integer noticePeriodInDays;

    @Column(nullable = false)
    @Min(0)
    private Double salaryBudget;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Min(0)
    private Double bonusPayPerCV;

    @Column(nullable = false)
    private String closureBonus;

    @Column
    private String comments;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    public Job(String client, String name, JobStatus status, Integer wantedCVs, String experienceRange, Integer noticePeriodInDays, Double salaryBudget, String description, Double bonusPayPerCV, String closureBonus, String comments) {
        this.client = client;
        this.name = name;
        this.status = status;
        this.wantedCVs = wantedCVs;
        this.experienceRange = experienceRange;
        this.noticePeriodInDays = noticePeriodInDays;
        this.salaryBudget = salaryBudget;
        this.description = description;
        this.bonusPayPerCV = bonusPayPerCV;
        this.closureBonus = closureBonus;
        this.comments = comments;
    }
}

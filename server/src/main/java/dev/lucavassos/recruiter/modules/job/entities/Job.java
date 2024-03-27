package dev.lucavassos.recruiter.modules.job.entities;

import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.skills.Skill;
import dev.lucavassos.recruiter.modules.user.entities.Role;
import jakarta.annotation.Nonnull;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Size(min = 1, message = "Job client must be at least 1 character long")
    private String client;

    @Column(nullable = false)
    @Size(min = 1, message = "Job name must be at least 1 character long")
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(nullable = false)
    @Min(value = 0, message = "Wanted CVs cannot be negative")
    private Integer wantedCVs;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "job_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "contract_type_id", referencedColumnName = "id")
    private ContractType contractType;

    @Column(nullable = false)
    @Min(value = 0, message = "Experience range cannot be negative")
    private Double experienceRange;

    @Column(nullable = false)
    @Min(value = 0, message = "Notice period cannot be negative")
    private Integer noticePeriodInDays;

    @Column(nullable = false)
    @Min(value = 0, message = "Salary budget cannot be negative")
    private Double salaryBudget;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Min(value = 0, message = "Bonus pay per CV cannot be negative")
    private Double bonusPayPerCV;

    @Column(nullable = false)
    private String closureBonus;

    @Column
    private String comments;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;
}

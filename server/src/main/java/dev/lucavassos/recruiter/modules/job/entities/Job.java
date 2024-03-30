package dev.lucavassos.recruiter.modules.job.entities;

import dev.lucavassos.recruiter.modules.candidacy.entities.Candidacy;
import dev.lucavassos.recruiter.modules.job.domain.Currency;
import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
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

    @Column(nullable = false, name = "client")
    @Size(min = 1, message = "Job client must be at least 1 character long")
    private String client;

    @Column(nullable = false, name = "name")
    @Size(min = 1, message = "Job name must be at least 1 character long")
    private String name;

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(nullable = false, name = "wanted_cvs")
    @Min(value = 0, message = "Wanted CVs cannot be negative")
    private Integer wantedCvs;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "jobs_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> skills = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_type_id")
    private ContractType contractType;

    @Column(nullable = false)
    @Min(value = 0, message = "Experience range minimum cannot be negative")
    private Integer experienceRangeMin;

    @Column(nullable = false)
    @Max(value = 100, message = "Experience range maximum cannot exceed 100")
    private Integer experienceRangeMax;

    @Column(nullable = false)
    @Min(value = 0, message = "Notice period cannot be negative")
    private Integer noticePeriodInDays;

    @Column(nullable = false)
    @Min(value = 0, message = "Salary budget cannot be negative")
    private Double salaryBudget;

    @Column(nullable = false, name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Min(value = 0, message = "Bonus pay per CV cannot be negative")
    private Double bonusPayPerCv;

    @Column(nullable = false)
    private String closureBonus;

    @Column
    private String comments;

    @OneToMany(mappedBy = "job")
    private Set<Candidacy> candidacies = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;
}

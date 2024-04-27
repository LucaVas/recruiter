package dev.lucavassos.recruiter.modules.job.entities;

import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.job.domain.Currency;
import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import dev.lucavassos.recruiter.modules.user.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name="jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

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
    private List<Skill> skills = new ArrayList<>();

    @Column(nullable = false, name = "contract_type")
    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    @Column(nullable = false)
    @Min(value = 0, message = "Experience range minimum cannot be negative")
    private Integer experienceRangeMin;

    @Column(nullable = false)
    @Max(value = 60, message = "Experience range maximum cannot exceed 60")
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
    @Min(value = 0, message = "Closure bonus cannot be negative")
    private Double closureBonus;

    @Column(nullable = false, name = "cv_rate_payment_date")
    private LocalDateTime cvRatePaymentDate;

    @Column(nullable = false, name = "closure_bonus_payment_date")
    private LocalDateTime closureBonusPaymentDate;

    private Integer numberOfCandidates;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id")
    private User recruiter;

    @CreationTimestamp
    @Column(nullable = false, name = "created_dtime")
    private LocalDateTime createdDTime;

    @UpdateTimestamp
    @Column(nullable = false, name = "modified_dtime")
    private LocalDateTime modifiedDTime;
}

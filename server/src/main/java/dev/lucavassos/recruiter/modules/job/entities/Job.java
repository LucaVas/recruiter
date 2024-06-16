package dev.lucavassos.recruiter.modules.job.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.lucavassos.recruiter.modules.candidacy.entities.Candidacy;
import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.job.domain.ContractType;
import dev.lucavassos.recruiter.modules.job.domain.Currency;
import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.questionnaire.entity.Questionnaire;
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
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name")
    @Size(min = 1, message = "Job name must be at least 1 character long")
    private String name;

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(nullable = false, name = "wanted_cvs")
    @Min(value = 0, message = "Wanted CVs cannot be negative")
    private Integer wantedCvs;

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

    @Column(nullable = false, length = 500)
    @Size(min = 1, max = 500, message = "Job name must be between 1 and 500 characters long")
    private String description;

    @Column(nullable = false)
    @Min(value = 0, message = "Bonus pay per CV cannot be negative")
    private Double bonusPayPerCv;

    @Column(nullable = false)
    @Size(min = 1, message = "Closure bonus name must be between 1 and 255 characters long")
    private String closureBonus;

    @Column(nullable = false, name = "cv_rate_payment_date")
    private LocalDateTime cvRatePaymentDate;

    @Column(nullable = false, name = "closure_bonus_payment_date")
    private LocalDateTime closureBonusPaymentDate;

    private Integer numberOfCandidates;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // relationships

    @OneToMany(
            mappedBy = "job",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private Set<Candidacy> candidacies = new HashSet<>();

    @ManyToOne
    @ToString.Exclude
    @JsonBackReference
    private User recruiter;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Questionnaire questionnaire;

    @ManyToMany
    @JoinTable(
            name = "job_skill",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @JsonManagedReference
    private Set<Skill> skills;
}

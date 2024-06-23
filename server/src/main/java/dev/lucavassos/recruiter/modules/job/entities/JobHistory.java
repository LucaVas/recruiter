package dev.lucavassos.recruiter.modules.job.entities;

import dev.lucavassos.recruiter.modules.HistoryEventType;
import dev.lucavassos.recruiter.modules.job.domain.ContractType;
import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.user.entities.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@Table(name = "jobs_history")
public class JobHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(nullable = false)
    private Integer wantedCvs;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    @Column(nullable = false)
    private Integer experienceRangeMin;

    @Column(nullable = false)
    private Integer experienceRangeMax;

    @Column(nullable = false)
    private Integer noticePeriodInDays;

    @Column(nullable = false)
    private Double salaryBudget;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private Double bonusPayPerCv;

    @Column(nullable = false)
    private String closureBonus;

    @Column(nullable = false)
    private LocalDateTime cvRatePaymentDate;

    @Column(nullable = false)
    private LocalDateTime closureBonusPaymentDate;

    @Column(nullable = false)
    private Integer numberOfCandidates;

    @Column(nullable = false)
    private HistoryEventType eventType;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Job job;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User modifiedBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}

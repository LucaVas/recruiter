package dev.lucavassos.recruiter.modules.job.entities;

import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
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
@Table(name = "jobs_history")
public class JobHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(nullable = false, name = "bonus_pay_per_cv")
    @Min(value = 0, message = "Bonus pay per CV cannot be negative")
    private Double bonusPayPerCv;

    @Column(nullable = false, name = "closure_bonus")
    @Size(min = 1, message = "Job name must be between 1 and 255 characters long")
    private String closureBonus;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User modifiedBy;

    @CreationTimestamp
    @Column(nullable = false, name = "created_dtime")
    private LocalDateTime createdDTime;

    @UpdateTimestamp
    @Column(nullable = false, name = "modified_dtime")
    private LocalDateTime modifiedDTime;
}

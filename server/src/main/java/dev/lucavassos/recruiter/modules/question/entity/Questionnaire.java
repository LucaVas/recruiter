package dev.lucavassos.recruiter.modules.question.entity;

import dev.lucavassos.recruiter.modules.job.entities.Job;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Builder
@Table(name="questionnaires")
public class Questionnaire {

    @Id
    @Column(nullable = false, name = "title", unique = true)
    @Size(min = 1, message = "Question title must be at least 1 character long")
    private String title;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy="questionnaire", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // actions on questionnaire are cascaded to questions
    private Set<Question> questions;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;
}

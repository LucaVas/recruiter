package dev.lucavassos.recruiter.modules.questionnaire.entity;

import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "questionnaires")
public class Questionnaire {

    @EmbeddedId
    private QuestionnaireId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("clientName")
    private Client client;

    @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    // actions on questionnaire are cascaded to questions
    private Set<Question> questions;

    @OneToMany(mappedBy = "questionnaire")
    private Set<Job> jobs;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

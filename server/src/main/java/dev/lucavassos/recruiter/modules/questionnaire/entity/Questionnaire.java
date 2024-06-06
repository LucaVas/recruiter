package dev.lucavassos.recruiter.modules.questionnaire.entity;

import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "questionnaires")
public class Questionnaire {

    @EmbeddedId
    private QuestionnaireId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("clientName")
    private Client client;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private Set<Question> questions = new HashSet<>();

    @OneToMany(mappedBy = "questionnaire")
    private Set<Job> jobs;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

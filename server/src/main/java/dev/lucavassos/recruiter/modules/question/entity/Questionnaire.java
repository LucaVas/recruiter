package dev.lucavassos.recruiter.modules.question.entity;

import dev.lucavassos.recruiter.modules.client.entities.Client;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@Table(name="questionnaires")
public class Questionnaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "title")
    @Size(min = 1, message = "Question title must be at least 1 character long")
    private String title;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="questionnaire_question",
            joinColumns=@JoinColumn(name="question_id"),
            inverseJoinColumns=@JoinColumn(name="questionnaire_id"))
    private Set<Question> questions;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "client_id")
    private Client client;

    @CreationTimestamp
    @Column(updatable = false, name = "created_dtime")
    private LocalDateTime createdDTime;

    @UpdateTimestamp
    @Column(name = "modified_dtime")
    private LocalDateTime modifiedDTime;
}

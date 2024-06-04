package dev.lucavassos.recruiter.modules.questionnaire.entity;

import dev.lucavassos.recruiter.modules.questionnaire.domain.QuestionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "text")
    private String text;

    @Column(name = "answer", length = 500)
    private String answer;

    @Column(nullable = false, name = "type")
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Questionnaire questionnaire;
}

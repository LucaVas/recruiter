package dev.lucavassos.recruiter.modules.question.entity;

import dev.lucavassos.recruiter.modules.question.domain.QuestionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Data
@Entity
@Builder
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

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private Questionnaire questionnaire;
}

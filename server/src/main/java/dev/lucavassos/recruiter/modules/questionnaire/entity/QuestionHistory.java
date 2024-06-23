package dev.lucavassos.recruiter.modules.questionnaire.entity;

import dev.lucavassos.recruiter.modules.HistoryEventType;
import dev.lucavassos.recruiter.modules.questionnaire.domain.QuestionType;
import dev.lucavassos.recruiter.modules.user.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@Table(name = "questions_history")
public class QuestionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID eventId;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false, length = 500)
    private String answer;

    @Column(nullable = false, name = "type")
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HistoryEventType eventType;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User modifiedBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Question question;
}

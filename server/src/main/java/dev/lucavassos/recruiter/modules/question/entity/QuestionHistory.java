package dev.lucavassos.recruiter.modules.question.entity;

import dev.lucavassos.recruiter.modules.question.domain.QuestionType;
import dev.lucavassos.recruiter.modules.user.entities.User;
import jakarta.persistence.*;
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
@Table(name="questions_history")
public class QuestionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false, name = "answer")
    private String answer;

    @Column(nullable = false)
    private Boolean active = true;


    @Column(nullable = false, name = "type")
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User modifiedBy;

    @CreationTimestamp
    @Column(updatable = false, name = "created_dtime")
    private LocalDateTime createdDTime;

    @UpdateTimestamp
    @Column(name = "modified_dtime")
    private LocalDateTime modifiedDTime;
}

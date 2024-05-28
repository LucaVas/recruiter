package dev.lucavassos.recruiter.modules.question.entity;

import dev.lucavassos.recruiter.modules.question.domain.QuestionType;
import jakarta.persistence.*;
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
@Table(name="questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "text")
    private String text;

    @Column(name = "answer", length = 500)
    private String answer;

    @Column(nullable = false, name = "active")
    private Boolean active = true;

    @Column(nullable = false, name = "type")
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @CreationTimestamp
    @Column(updatable = false, name = "created_dtime")
    private LocalDateTime createdDTime;

    @UpdateTimestamp
    @Column(name = "modified_dtime")
    private LocalDateTime modifiedDTime;
}

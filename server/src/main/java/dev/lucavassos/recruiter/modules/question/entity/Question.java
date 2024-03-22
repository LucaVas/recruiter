package dev.lucavassos.recruiter.modules.question.entity;

import dev.lucavassos.recruiter.modules.skills.Skill;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, message = "Question text must be at least 1 character long")
    private String text;

    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name="skill_id")
    private Skill skill;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    public Question(String text) {
        this.text = text;
    }

    public Question(String text, Boolean active) {
        this.text = text;
        this.active = active;
    }
}

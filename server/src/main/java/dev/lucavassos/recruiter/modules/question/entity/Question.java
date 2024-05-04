package dev.lucavassos.recruiter.modules.question.entity;

import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

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

    @Column(nullable = false, name = "title")
    @Size(min = 1, message = "Question title must be at least 1 character long")
    private String title;

    @Column(nullable = false, name = "text")
    @Size(min = 1, message = "Question text must be at least 1 character long")
    private String text;

    @Column(nullable = false, name = "answer", length = 500)
    @Size(min = 1, message = "Question answer must be at least 1 character long")
    private String answer;

    @Column(nullable = false, name = "active")
    private Boolean active = true;

    @Column(name = "division")
    private String division;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="skill_question",
            joinColumns=@JoinColumn(name="skill_id"),
            inverseJoinColumns=@JoinColumn(name="question_id"))
    private Set<Skill> skills;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @CreationTimestamp
    @Column(updatable = false, name = "created_dtime")
    private LocalDateTime createdDTime;

    @UpdateTimestamp
    @Column(name = "modified_dtime")
    private LocalDateTime modifiedDTime;
}

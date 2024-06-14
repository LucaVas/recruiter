package dev.lucavassos.recruiter.modules.client.entities;

import dev.lucavassos.recruiter.modules.client.domain.Industry;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.questionnaire.entity.Questionnaire;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
@Table(name = "clients")
public class Client {

    @Id
    @Column(name = "name")
    @Size(min = 1, message = "Client name must be at least 1 character long")
    private String name;

    @Column(nullable = false, name = "industry")
    @Enumerated(EnumType.STRING)
    private Industry industry;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "client",
            cascade = CascadeType.REMOVE, // delete jobs associated to client
            orphanRemoval = true)
    private Set<Job> jobs = new HashSet<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Questionnaire> questionnaires = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

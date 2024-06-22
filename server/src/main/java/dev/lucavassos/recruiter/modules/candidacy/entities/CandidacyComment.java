package dev.lucavassos.recruiter.modules.candidacy.entities;

import dev.lucavassos.recruiter.modules.user.entities.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@Setter
@Getter
@EqualsAndHashCode
@Table(name = "candidacy_comments")
public class CandidacyComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "text", length = 500)
    private String text;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // relationships

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidacy_id")
    @ToString.Exclude
    private Candidacy candidacy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private User author;
}

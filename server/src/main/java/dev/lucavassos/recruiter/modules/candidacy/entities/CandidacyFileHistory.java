package dev.lucavassos.recruiter.modules.candidacy.entities;

import dev.lucavassos.recruiter.modules.HistoryEventType;
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
@ToString
@Builder
@Table(name = "candidacy_files_history")
public class CandidacyFileHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID eventId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private UUID uniqueId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HistoryEventType eventType;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CandidacyFile candidacyFile;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User modifiedBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}

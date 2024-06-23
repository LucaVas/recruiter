package dev.lucavassos.recruiter.modules.candidacy.entities;

import dev.lucavassos.recruiter.modules.HistoryEventType;
import dev.lucavassos.recruiter.modules.candidacy.domain.CandidacyStatus;
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
@Table(name = "candidacy_history")
public class CandidacyHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID eventId;

    @Column(nullable = false)
    private double relevantExperience;

    @Column(nullable = false)
    private double expectedCtc;

    @Column(nullable = false)
    private double officialNoticePeriod;

    @Column(nullable = false)
    private double actualNoticePeriod;

    @Column
    private String reasonForQuickJoin;

    @Column(nullable = false, name = "candidacy_status")
    @Enumerated(EnumType.STRING)
    private CandidacyStatus status;

    @Column(nullable = false)
    private HistoryEventType eventType;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Candidacy candidacy;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User modifiedBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}

package dev.lucavassos.recruiter.modules.candidacy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="candidacy_file")
public class CandidacyFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "type")
    private String type;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "unique_id")
    private String uniqueId;

    @Column(name = "reason_for_quick_join")
    private String reasonForQuickJoin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "job_id", referencedColumnName = "job_id"),
            @JoinColumn(name = "candidate_pan", referencedColumnName = "candidate_pan")
    })
    private Candidacy candidacy;

    @CreationTimestamp
    @Column(updatable = false, name = "created_dtime")
    private LocalDateTime createdDTime;

    @UpdateTimestamp
    @Column(name = "modified_dtime")
    private LocalDateTime modifiedDTime;
}

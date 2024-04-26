package dev.lucavassos.recruiter.modules.client.entities;

import dev.lucavassos.recruiter.modules.client.domain.Industry;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name")
    @Size(min = 1, message = "Job name must be at least 1 character long")
    private String name;

    @Column(nullable = false, name = "industry")
    @Enumerated(EnumType.STRING)
    private Industry industry;

    @OneToMany(
            mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Job> jobs = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false, name = "created_dtime")
    private LocalDateTime createdDTime;

    @UpdateTimestamp
    @Column(name = "modified_dtime")
    private LocalDateTime modifiedDTime;
}

package dev.lucavassos.recruiter.modules.client.entities;

import dev.lucavassos.recruiter.modules.client.domain.Industry;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@Table(name="clients")
public class Client implements Serializable {

    @Serial
    private static final long serialVersionUID = -7049957706738879274L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name")
    @Size(min = 1, message = "Job name must be at least 1 character long")
    private String name;

    @Column(nullable = false, name = "industry")
    @Enumerated(EnumType.STRING)
    private Industry industry;

    @CreationTimestamp
    @Column(updatable = false, name = "created_dtime")
    private LocalDateTime createdDTime;

    @UpdateTimestamp
    @Column(name = "modified_dtime")
    private LocalDateTime modifiedDTime;
}

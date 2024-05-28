package dev.lucavassos.recruiter.modules.client.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Builder
@Table(name="clients_error_logs")
public class ClientErrorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name")
    private String description;

    @Column(nullable = false, name = "code")
    private Integer code;

    @CreationTimestamp
    @Column(nullable = false, name = "created_dtime")
    private LocalDateTime createdDTime;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_dtime")
    private LocalDateTime updatedDTime;
}

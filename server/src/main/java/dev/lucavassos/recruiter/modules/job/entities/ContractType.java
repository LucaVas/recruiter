package dev.lucavassos.recruiter.modules.job.entities;

import dev.lucavassos.recruiter.modules.user.entities.ContractTypeName;
import dev.lucavassos.recruiter.modules.user.entities.RoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "contract_types")
public class ContractType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private ContractTypeName contractTypeName;

    @OneToOne(mappedBy = "contractType")
    private Job job;
}

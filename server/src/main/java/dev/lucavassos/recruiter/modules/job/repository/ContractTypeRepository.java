package dev.lucavassos.recruiter.modules.job.repository;

import dev.lucavassos.recruiter.modules.job.entities.ContractType;
import dev.lucavassos.recruiter.modules.job.entities.ContractTypeName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractTypeRepository extends JpaRepository<ContractType, Long> {
    Optional<ContractType> findByContractTypeName(ContractTypeName contractTypeName);
}

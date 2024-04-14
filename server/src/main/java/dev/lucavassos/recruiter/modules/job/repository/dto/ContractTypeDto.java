package dev.lucavassos.recruiter.modules.job.repository.dto;

import dev.lucavassos.recruiter.modules.job.entities.ContractTypeName;
import jakarta.validation.constraints.NotNull;

public record ContractTypeDto(
        @NotNull ContractTypeName contractTypeName
) {
}

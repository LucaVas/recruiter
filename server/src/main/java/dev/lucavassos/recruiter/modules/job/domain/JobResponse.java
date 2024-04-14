package dev.lucavassos.recruiter.modules.job.domain;


import dev.lucavassos.recruiter.modules.job.repository.dto.JobDto;
import jakarta.validation.constraints.NotNull;

public record JobResponse(
        @NotNull Long id,
        @NotNull JobDto job
) {

}

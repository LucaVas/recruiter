package dev.lucavassos.recruiter.modules.job.domain;


import dev.lucavassos.recruiter.modules.job.repository.dto.JobDto;

public record JobResponse(
        Long id,
        JobDto job
) {}

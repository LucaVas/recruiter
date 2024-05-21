package dev.lucavassos.recruiter.modules.job.domain;


import jakarta.validation.constraints.NotNull;

public record ChangeJobStatusRequest(
        @NotNull(message = "Job status is required")
        JobStatus status
) {}

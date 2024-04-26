package dev.lucavassos.recruiter.modules.client.repository.dto;

import dev.lucavassos.recruiter.modules.client.domain.Industry;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ClientDto(
        @NotNull Long id,
        @NotBlank(message = "Client name cannot be empty") String name,
        @NotNull(message = "Industry cannot be empty") Industry industry,
        LocalDateTime createdDTime
) {}

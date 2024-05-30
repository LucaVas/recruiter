package dev.lucavassos.recruiter.modules.client.repository.dto;

import dev.lucavassos.recruiter.modules.client.domain.Industry;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ClientDto(
        Long id,
        @NotBlank(message = "Client name is required") String name,
        @NotNull(message = "Client industry is required") Industry industry
) {}

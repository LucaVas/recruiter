package dev.lucavassos.recruiter.modules.client.repository.dto;

import dev.lucavassos.recruiter.modules.client.domain.Industry;

import java.time.LocalDateTime;

public record ClientDto(
        Long id,
        String name,
        Industry industry,
        LocalDateTime createdDTime,
        LocalDateTime modifiedDTime
) {}

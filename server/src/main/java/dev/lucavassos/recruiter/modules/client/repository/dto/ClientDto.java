package dev.lucavassos.recruiter.modules.client.repository.dto;

import dev.lucavassos.recruiter.modules.client.domain.Industry;

public record ClientDto(
        String name,
        Industry industry
) {}

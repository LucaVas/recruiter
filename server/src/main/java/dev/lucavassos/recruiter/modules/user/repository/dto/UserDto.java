package dev.lucavassos.recruiter.modules.user.repository.dto;

import dev.lucavassos.recruiter.modules.user.entities.Role;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String name,
        String email,
        String phone,
        String city,
        String country,
        Role role,
        Boolean approved,
        ApproverDto approver,
        String comment,
        LocalDateTime approvedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

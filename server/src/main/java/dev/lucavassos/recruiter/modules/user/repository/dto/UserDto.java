package dev.lucavassos.recruiter.modules.user.repository.dto;

import dev.lucavassos.recruiter.modules.user.entities.Role;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String username,
        String email,
        String mobile,
        String city,
        String country,
        java.util.Set<Role> role,
        String comments,
        Boolean approved,
        ApproverDto approver,
        LocalDateTime approvedOn
) {
}

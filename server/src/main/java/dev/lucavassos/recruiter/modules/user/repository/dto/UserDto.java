package dev.lucavassos.recruiter.modules.user.repository.dto;

import dev.lucavassos.recruiter.modules.user.entities.Role;

import java.time.LocalDateTime;
import java.util.Set;

public record UserDto(
        Long id,
        String username,
        String email,
        String mobile,
        String city,
        String country,
        Set<Role> roles,
        String comments,
        Boolean approved,
        ApproverDto approver,
        LocalDateTime approvedOn
) {
}

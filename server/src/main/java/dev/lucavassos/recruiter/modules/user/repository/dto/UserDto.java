package dev.lucavassos.recruiter.modules.user.repository.dto;

import dev.lucavassos.recruiter.modules.user.domain.Role;

import java.time.LocalDateTime;
import java.util.Date;

public record UserDto(
        Long id,
        String username,
        String email,
        String mobile,
        String city,
        String country,
        Role role,
        String comments,
        Boolean approved,
        ApproverDto approver,
        LocalDateTime approvedOn
) {
}

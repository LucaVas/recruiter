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
        Boolean approved,
        ApproverDto approver,
        String comment,
        LocalDateTime approvedDTime,
        LocalDateTime createdDTime,
        LocalDateTime modifiedDTime
) {
}

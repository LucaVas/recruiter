package dev.lucavassos.recruiter.auth.domain;

import dev.lucavassos.recruiter.modules.user.entities.RoleName;

public record AuthUserInfoDto(
        Long id,
        String username,
        RoleName roleName
) {
}

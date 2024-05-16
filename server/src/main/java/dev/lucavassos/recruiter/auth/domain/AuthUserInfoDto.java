package dev.lucavassos.recruiter.auth.domain;

import dev.lucavassos.recruiter.modules.user.domain.RoleName;

public record AuthUserInfoDto(
        Long id,
        String name,
        RoleName roleName
) {
}

package dev.lucavassos.recruiter.modules.user.repository.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public final class RecruiterDto {
    private final Long id;
    private final String username;
}

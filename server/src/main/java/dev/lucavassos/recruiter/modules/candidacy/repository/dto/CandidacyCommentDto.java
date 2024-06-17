package dev.lucavassos.recruiter.modules.candidacy.repository.dto;

import dev.lucavassos.recruiter.modules.user.repository.dto.UserDto;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public final class CandidacyCommentDto {
    private final Long id;
    private final String text;
    private final UserDto author;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}

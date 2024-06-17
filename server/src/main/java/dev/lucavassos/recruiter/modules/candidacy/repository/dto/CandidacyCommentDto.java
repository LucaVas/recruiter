package dev.lucavassos.recruiter.modules.candidacy.repository.dto;

import dev.lucavassos.recruiter.modules.user.repository.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public final class CandidacyCommentDto {
    private final Long id;
    private final String text;
    private final UserDto author;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}

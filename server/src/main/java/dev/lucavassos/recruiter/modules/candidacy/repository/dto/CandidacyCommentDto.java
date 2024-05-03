package dev.lucavassos.recruiter.modules.candidacy.repository.dto;

import dev.lucavassos.recruiter.modules.user.repository.dto.UserDto;

import java.time.LocalDateTime;

public record CandidacyCommentDto(
        Long id,
        String text,
        UserDto author,
        LocalDateTime createdDTime,
        LocalDateTime modifiedDTime
) {
}

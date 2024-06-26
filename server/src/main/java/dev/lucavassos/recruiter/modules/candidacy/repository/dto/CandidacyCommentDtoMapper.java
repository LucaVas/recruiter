package dev.lucavassos.recruiter.modules.candidacy.repository.dto;

import dev.lucavassos.recruiter.modules.candidacy.entities.CandidacyComment;
import dev.lucavassos.recruiter.modules.user.repository.dto.UserDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CandidacyCommentDtoMapper implements Function<CandidacyComment, CandidacyCommentDto> {

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Override
    public CandidacyCommentDto apply(CandidacyComment comment) {
        return CandidacyCommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(userDtoMapper.apply(comment.getAuthor()))
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}

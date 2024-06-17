package dev.lucavassos.recruiter.modules.user.repository.dto;

import dev.lucavassos.recruiter.modules.user.entities.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDtoMapper implements Function<User, UserDto> {
    @Override
    public UserDto apply(User user) {

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getUsername())
                .phone(user.getPhone())
                .city(user.getCity())
                .country(user.getCountry())
                .role(user.getRole())
                .approved(user.isApproved())
                .approver(ApproverDto.builder()
                        .id(user.getId())
                        .username(user.getName())
                        .build())
                .comment(user.getComment())
                .approvedAt(user.getApprovedAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}

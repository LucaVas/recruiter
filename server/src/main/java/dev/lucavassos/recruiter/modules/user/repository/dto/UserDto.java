package dev.lucavassos.recruiter.modules.user.repository.dto;

import dev.lucavassos.recruiter.modules.user.entities.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public final class UserDto {
    private final Long id;
    private final String name;
    private final String email;
    private final String phone;
    private final String city;
    private final String country;
    private final Role role;
    private final Boolean approved;
    private final ApproverDto approver;
    private final String comment;
    private final LocalDateTime approvedAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}

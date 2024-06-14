package dev.lucavassos.recruiter.modules.user.repository.dto;

import dev.lucavassos.recruiter.modules.user.entities.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDtoMapper implements Function<User, UserDto> {
    @Override
    public UserDto apply(User user) {

        return new UserDto(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getPhone(),
                user.getCity(),
                user.getCountry(),
                user.getRole(),
                user.isApproved(),
                new ApproverDto(
                        user.getId(),
                        user.getName()
                ),
                user.getComment(),
                user.getApprovedAt(),
                user.getCreatedAt(),
                user.getUpdatedAt()

        );
    }
}

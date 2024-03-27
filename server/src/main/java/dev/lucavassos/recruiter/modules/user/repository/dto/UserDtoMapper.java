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
                user.getUsername(),
                user.getEmail(),
                user.getMobile(),
                user.getCity(),
                user.getCountry(),
                user.getRoles(),
                user.getComments(),
                user.isApproved(),
                new ApproverDto(
                        user.getId(),
                        user.getUsername()
                ),
                user.getApprovedOn()
        );
    }
}

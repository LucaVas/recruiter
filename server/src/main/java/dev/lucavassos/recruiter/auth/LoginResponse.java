package dev.lucavassos.recruiter.auth;

import dev.lucavassos.recruiter.modules.user.repository.dto.UserDto;

public record AuthenticationResponse(
        String token,
        UserDto userDto
){
}

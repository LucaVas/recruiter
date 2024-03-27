package dev.lucavassos.recruiter.auth;

import dev.lucavassos.recruiter.modules.user.repository.dto.UserDto;

public record LoginResponse(
        String token,
        String tokenType
){
}

package dev.lucavassos.recruiter.modules.user.domain;

public record LoginResponse(
        String token,
        String username
){
}

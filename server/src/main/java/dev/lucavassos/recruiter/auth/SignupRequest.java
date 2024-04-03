package dev.lucavassos.recruiter.auth;

import dev.lucavassos.recruiter.modules.user.entities.Role;
import dev.lucavassos.recruiter.modules.user.entities.RoleName;

public record SignupRequest(
        String username,
        String email,
        String password,
        String mobile,
        String city,
        String country,
        String roleName
) {

}

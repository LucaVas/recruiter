package dev.lucavassos.recruiter.modules.user.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserApprovalRequest(

        @NotNull Long userId,

        @NotNull(message = "User approved value is required")
        Boolean approved,

        @NotBlank(message = "User approval comment is required")
        @Length(max = 255, message = "User approval comment must be less than 255 characters")
        String comment
) {
}

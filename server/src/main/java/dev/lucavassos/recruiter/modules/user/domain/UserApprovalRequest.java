package dev.lucavassos.recruiter.modules.user.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserApprovalRequest(

        @NotNull Long userId,

        @NotNull(message = "Approved value cannot be empty")
        Boolean approved,

        @NotBlank(message = "Comment cannot be empty")
        @Length(min = 1, max = 255, message = "Comment must be between 1 and 500 characters")
        String commment 
) {
}

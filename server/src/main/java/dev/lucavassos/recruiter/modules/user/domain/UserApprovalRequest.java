package dev.lucavassos.recruiter.modules.user.domain;

import jakarta.validation.constraints.NotNull;

public record UserApprovalRequest(

        @NotNull Long userId,

        @NotNull(message = "Approved value cannot be empty")
        Boolean approved,

        @NotNull(message = "Comments cannot be empty")
        String commments
) {
}

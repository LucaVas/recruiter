package dev.lucavassos.recruiter.modules.user.domain;

public record UserApprovalRequest(
        Long userId,
        Boolean approved,
        String commments
) {
}

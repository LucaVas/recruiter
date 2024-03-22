package dev.lucavassos.recruiter.modules.user.domain;

public record UserApprovalRequest(
        Boolean approved,
        String commments
) {
}

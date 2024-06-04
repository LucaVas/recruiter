package dev.lucavassos.recruiter.modules.user.repository;

import dev.lucavassos.recruiter.modules.user.entities.PasswordResetToken;
import dev.lucavassos.recruiter.modules.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findOneByTokenString(String tokenString);

    void deleteAllByUser(User user);
}

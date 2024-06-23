package dev.lucavassos.recruiter.modules.user.repository;

import dev.lucavassos.recruiter.modules.user.entities.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
}

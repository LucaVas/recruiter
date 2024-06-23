package dev.lucavassos.recruiter.modules.client.repository;

import dev.lucavassos.recruiter.modules.client.entities.ClientHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientHistoryRepository extends JpaRepository<ClientHistory, Long> {
}

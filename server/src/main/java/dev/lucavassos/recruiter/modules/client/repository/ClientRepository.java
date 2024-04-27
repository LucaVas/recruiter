package dev.lucavassos.recruiter.modules.client.repository;

import dev.lucavassos.recruiter.modules.client.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long>  {
    Optional<Client> findById(Long id);
    Optional<Client> finaByName(String name);
}

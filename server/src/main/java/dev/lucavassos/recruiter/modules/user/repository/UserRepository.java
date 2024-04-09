package dev.lucavassos.recruiter.modules.user.repository;

import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);
    boolean existsUserByMobile(String mobile);
    boolean existsUserByUsername(String username);

    Optional<User> findOneByEmail(String email);
    Optional<User> findOneById(Long id);
    Optional<User> findOneByUsername(String username);
    Optional<User> findByUsernameOrEmail(String username, String email);

}

package dev.lucavassos.recruiter.modules.user.repository;

import dev.lucavassos.recruiter.modules.user.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // method which checks if user is approved
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.approved = true")
    Optional<User> findApprovedUserById(@Param("id") Long id);
    boolean existsUserByEmail(String email);
    boolean existsUserByPhone(String phone);
    boolean existsUserByName(String name);
    @Query("SELECT u FROM User u WHERE u.id <> :id")
    Page<User> findAllButMe(@Param("id") Long id, Pageable pageable);
    Optional<User> findOneByEmail(String email);
    Optional<User> findOneById(Long id);
    Optional<User> findOneByName(String name);
}

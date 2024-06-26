package dev.lucavassos.recruiter.modules.skill.repository;

import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SkillRepository extends JpaRepository<Skill, Long> {
    Boolean existsByName(String name);

    Optional<Skill> findByName(String name);
}

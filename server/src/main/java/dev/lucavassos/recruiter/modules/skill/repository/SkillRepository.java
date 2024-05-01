package dev.lucavassos.recruiter.modules.skill.repository;

import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SkillRepository extends JpaRepository<Skill, Long> {

    Optional<Skill> findByName(String name);
}

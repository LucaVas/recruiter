package dev.lucavassos.recruiter.modules.job.repository;

import dev.lucavassos.recruiter.modules.job.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<List<Job>> findByName(String name);
    Optional<Job> findOneById(Long id);
//    @Modifying(clearAutomatically = true)
//    @Query(value = "SELECT j.id, j.client, j.name, " +
//            "j.status, j.contract_type_id, ct.contract_type_name, " +
//            "j.wanted_cvs, j.experience_range, " +
//            "s.name as skill_name, j.notice_period_in_days, j.salary_budget, " +
//            "j.description, j.bonus_pay_per_cv, " +
//            "j.closure_bonus, j.comments, j.created_at, j.modified_at " +
//            "FROM jobs j " +
//            "LEFT JOIN job_skills js ON j.id = js.job_id " +
//            "LEFT JOIN skills s ON s.id = js.skill_id " +
//            "LEFT JOIN contract_types ct ON ct.id = j.contract_type_id;",
//    nativeQuery = true)
//
//    List<Job> getJobsWithSkills();

}

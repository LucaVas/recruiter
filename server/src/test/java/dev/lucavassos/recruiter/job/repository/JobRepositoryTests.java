package dev.lucavassos.recruiter.job.repository;

import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.job.repository.JobRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static dev.lucavassos.recruiter.testUtils.RandomUtils.randomNumber;
import static dev.lucavassos.recruiter.testUtils.RandomUtils.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
public class JobRepositoryTests {

    @Autowired
    private JobRepository repository;

    @Autowired
    private TestEntityManager manager;


    @Test
    public void save_creates_new_job() {

        String jobName = randomString(10, 50);
        Job job = Job.builder()
                .client(randomString(5, 15))
                .name(jobName)
                .status(JobStatus.OPEN)
                .wantedCvs(randomNumber(1, 10))
                .experienceRange(1.5)
                .noticePeriodInDays(randomNumber(1, 20))
                .salaryBudget(1500.00)
                .description(randomString(10, 100))
                .bonusPayPerCV(10.5)
                .closureBonus(randomString(5, 20))
                .build();

        Job savedJob = repository.save(job);

        Job existsJob = manager.find(Job.class, savedJob.getId());

        assertThat(existsJob.getName()).isEqualTo(jobName);
        assertThat(existsJob.getStatus()).isEqualTo(JobStatus.OPEN);
    }

    @Test
    public void save_throws_error_if_client_is_invalid() {
        Job jobWithShortClient = Job.builder()
                .client("")
                .name(randomString(3, 10))
                .status(JobStatus.OPEN)
                .wantedCVs(randomNumber(1, 10))
                .experienceRange(1.5)
                .noticePeriodInDays(randomNumber(1, 20))
                .salaryBudget(1500.00)
                .description(randomString(10, 100))
                .bonusPayPerCV(10.5)
                .closureBonus(randomString(5, 20))
                .build();

        assertThatThrownBy(() -> {
            repository.save(jobWithShortClient);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Job client must be at least 1 character long");
    }

    @Test
    public void save_throws_error_if_name_is_invalid() {
        Job jobWithShortName = Job.builder()
                .client(randomString(3, 10))
                .name("")
                .status(JobStatus.OPEN)
                .wantedCVs(randomNumber(1, 10))
                .experienceRange(1.5)
                .noticePeriodInDays(randomNumber(1, 20))
                .salaryBudget(1500.00)
                .description(randomString(10, 100))
                .bonusPayPerCV(10.5)
                .closureBonus(randomString(5, 20))
                .build();

        assertThatThrownBy(() -> {
            repository.save(jobWithShortName);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Job name must be at least 1 character long");
    }

    @Test
    public void save_throws_error_if_wanted_cvs_is_invalid() {
        Job jobWithNegativeWantedCvs = Job.builder()
                .client(randomString(3, 10))
                .name(randomString(3, 10))
                .status(JobStatus.OPEN)
                .wantedCVs(-1)
                .experienceRange(1.5)
                .noticePeriodInDays(randomNumber(1, 20))
                .salaryBudget(1500.00)
                .description(randomString(10, 100))
                .bonusPayPerCV(10.5)
                .closureBonus(randomString(5, 20))
                .build();

        assertThatThrownBy(() -> {
            repository.save(jobWithNegativeWantedCvs);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Wanted CVs cannot be negative");
    }

    @Test
    public void save_throws_error_if_experience_range_is_invalid() {
        Job jobWithNegativeBonusPayPerCV = Job.builder()
                .client(randomString(3, 10))
                .name(randomString(3, 10))
                .status(JobStatus.OPEN)
                .wantedCVs(randomNumber(1,5))
                .experienceRange(-1.5)
                .noticePeriodInDays(randomNumber(1, 20))
                .salaryBudget(1500.00)
                .description(randomString(10, 100))
                .bonusPayPerCV(10.5)
                .closureBonus(randomString(5, 20))
                .build();

        assertThatThrownBy(() -> {
            repository.save(jobWithNegativeBonusPayPerCV);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Experience range cannot be negative");
    }

    @Test
    public void save_throws_error_if_notice_period_is_invalid() {
        Job jobWithNegativeBonusPayPerCV = Job.builder()
                .client(randomString(3, 10))
                .name(randomString(3, 10))
                .status(JobStatus.OPEN)
                .wantedCVs(randomNumber(1,5))
                .experienceRange(1.5)
                .noticePeriodInDays(-1)
                .salaryBudget(1500.00)
                .description(randomString(10, 100))
                .bonusPayPerCV(10.5)
                .closureBonus(randomString(5, 20))
                .build();

        assertThatThrownBy(() -> {
            repository.save(jobWithNegativeBonusPayPerCV);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Notice period cannot be negative");
    }

    @Test
    public void save_throws_error_if_salary_budget_is_invalid() {
        Job jobWithNegativeBonusPayPerCV = Job.builder()
                .client(randomString(3, 10))
                .name(randomString(3, 10))
                .status(JobStatus.OPEN)
                .wantedCVs(randomNumber(1,5))
                .experienceRange(1.5)
                .noticePeriodInDays(randomNumber(1, 5))
                .salaryBudget(-1500.00)
                .description(randomString(10, 100))
                .bonusPayPerCV(10.5)
                .closureBonus(randomString(5, 20))
                .build();

        assertThatThrownBy(() -> {
            repository.save(jobWithNegativeBonusPayPerCV);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Salary budget cannot be negative");
    }

    @Test
    public void save_throws_error_if_bonus_pay_per_CV_is_invalid() {
        Job jobWithNegativeBonusPayPerCV = Job.builder()
                .client(randomString(3, 10))
                .name(randomString(3, 10))
                .status(JobStatus.OPEN)
                .wantedCVs(randomNumber(1,5))
                .experienceRange(1.5)
                .noticePeriodInDays(randomNumber(1, 5))
                .salaryBudget(1500.00)
                .description(randomString(10, 100))
                .bonusPayPerCV(-10.5)
                .closureBonus(randomString(5, 20))
                .build();

        assertThatThrownBy(() -> {
            repository.save(jobWithNegativeBonusPayPerCV);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Bonus pay per CV cannot be negative");
    }
}

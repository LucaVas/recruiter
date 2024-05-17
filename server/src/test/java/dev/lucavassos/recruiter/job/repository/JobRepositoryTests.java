package dev.lucavassos.recruiter.job.repository;

import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.job.domain.Currency;
import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.job.entities.ContractType;
import dev.lucavassos.recruiter.modules.job.entities.Job;
import dev.lucavassos.recruiter.modules.job.repository.JobRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;

import static dev.lucavassos.recruiter.testUtils.RandomUtils.*;
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

        String name = randomString(10, 50);
        Job job = Job.builder()
                .client(Client.builder().name(randomString(10, 50)).build())
                .name(name)
                .status(JobStatus.OPEN)
                .wantedCvs(randomInteger(1, 10))
                .contractType(ContractType.PERMANENT)
                .experienceRangeMin(randomInteger(0, 50))
                .experienceRangeMax(randomInteger(0, 50))
                .noticePeriodInDays(randomInteger(1, 20))
                .salaryBudget(1500.00)
                .currency(Currency.INR)
                .description(randomString(10, 100))
                .bonusPayPerCv(randomDouble(1, 1000))
                .closureBonus(randomString(1, 100))
                .closureBonusPaymentDate(LocalDateTime.now())
                .cvRatePaymentDate(LocalDateTime.now())
                .build();

        Job savedJob = repository.save(job);

        Job existsJob = manager.find(Job.class, savedJob.getId());

        assertThat(existsJob.getName()).isEqualTo(name);
        assertThat(existsJob.getStatus()).isEqualTo(JobStatus.OPEN);
    }

    @Test
    public void save_throws_error_if_name_is_invalid() {
        Job job = Job.builder()
                .client(Client.builder().name(randomString(10, 50)).build())
                .name("")
                .status(JobStatus.OPEN)
                .wantedCvs(randomInteger(1, 10))
                .contractType(ContractType.PERMANENT)
                .experienceRangeMin(randomInteger(0, 50))
                .experienceRangeMax(randomInteger(0, 50))
                .noticePeriodInDays(randomInteger(1, 20))
                .salaryBudget(1500.00)
                .currency(Currency.INR)
                .description(randomString(10, 100))
                .bonusPayPerCv(randomDouble(1, 1000))
                .closureBonus(randomString(1, 100))
                .closureBonusPaymentDate(LocalDateTime.now())
                .cvRatePaymentDate(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> {
            repository.save(job);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Job name must be at least 1 character long");
    }

    @Test
    public void save_throws_error_if_wanted_cvs_is_invalid() {
        Job job = Job.builder()
                .client(Client.builder().name(randomString(10, 50)).build())
                .name(randomString(10, 50))
                .status(JobStatus.OPEN)
                .wantedCvs(-1)
                .contractType(ContractType.PERMANENT)
                .experienceRangeMin(randomInteger(0, 50))
                .experienceRangeMax(randomInteger(0, 50))
                .noticePeriodInDays(randomInteger(1, 20))
                .salaryBudget(1500.00)
                .currency(Currency.INR)
                .description(randomString(10, 100))
                .bonusPayPerCv(randomDouble(1, 1000))
                .closureBonus(randomString(1, 100))
                .closureBonusPaymentDate(LocalDateTime.now())
                .cvRatePaymentDate(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> {
            repository.save(job);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Wanted CVs cannot be negative");
    }

    @Test
    public void save_throws_error_if_experience_range_min_is_invalid() {
        Job job = Job.builder()
                .client(Client.builder().name(randomString(10, 50)).build())
                .name(randomString(10, 50))
                .status(JobStatus.OPEN)
                .wantedCvs(randomInteger(1, 10))
                .contractType(ContractType.PERMANENT)
                .experienceRangeMin(-1)
                .experienceRangeMax(randomInteger(0, 50))
                .noticePeriodInDays(randomInteger(1, 20))
                .salaryBudget(1500.00)
                .currency(Currency.INR)
                .description(randomString(10, 100))
                .bonusPayPerCv(randomDouble(1, 1000))
                .closureBonus(randomString(1, 100))
                .closureBonusPaymentDate(LocalDateTime.now())
                .cvRatePaymentDate(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> {
            repository.save(job);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Experience range minimum cannot be negative");
    }

    @Test
    public void save_throws_error_if_experience_range_max_is_invalid() {
        Job job = Job.builder()
                .client(Client.builder().name(randomString(10, 50)).build())
                .name(randomString(10, 50))
                .status(JobStatus.OPEN)
                .wantedCvs(randomInteger(1, 10))
                .contractType(ContractType.PERMANENT)
                .experienceRangeMin(randomInteger(0, 50))
                .experienceRangeMax(61)
                .noticePeriodInDays(randomInteger(1, 20))
                .salaryBudget(1500.00)
                .currency(Currency.INR)
                .description(randomString(10, 100))
                .bonusPayPerCv(randomDouble(1, 1000))
                .closureBonus(randomString(1, 100))
                .closureBonusPaymentDate(LocalDateTime.now())
                .cvRatePaymentDate(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> {
            repository.save(job);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Experience range maximum cannot exceed 60");
    }

    @Test
    public void save_throws_error_if_notice_period_is_invalid() {
        Job job = Job.builder()
                .client(Client.builder().name(randomString(10, 50)).build())
                .name(randomString(10, 50))
                .status(JobStatus.OPEN)
                .wantedCvs(randomInteger(1, 10))
                .contractType(ContractType.PERMANENT)
                .experienceRangeMin(randomInteger(0, 50))
                .experienceRangeMax(randomInteger(0, 50))
                .noticePeriodInDays(-1)
                .salaryBudget(1500.00)
                .currency(Currency.INR)
                .description(randomString(10, 100))
                .bonusPayPerCv(randomDouble(1, 1000))
                .closureBonus(randomString(1, 100))
                .closureBonusPaymentDate(LocalDateTime.now())
                .cvRatePaymentDate(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> {
            repository.save(job);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Notice period cannot be negative");
    }

    @Test
    public void save_throws_error_if_salary_budget_is_invalid() {
        Job job = Job.builder()
                .client(Client.builder().name(randomString(10, 50)).build())
                .name(randomString(10, 50))
                .status(JobStatus.OPEN)
                .wantedCvs(randomInteger(1, 10))
                .contractType(ContractType.PERMANENT)
                .experienceRangeMin(randomInteger(0, 50))
                .experienceRangeMax(randomInteger(0, 50))
                .noticePeriodInDays(randomInteger(1, 20))
                .salaryBudget(-1.0)
                .currency(Currency.INR)
                .description(randomString(10, 100))
                .bonusPayPerCv(randomDouble(1, 1000))
                .closureBonus(randomString(1, 100))
                .closureBonusPaymentDate(LocalDateTime.now())
                .cvRatePaymentDate(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> {
            repository.save(job);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Salary budget cannot be negative");
    }

    @Test
    public void save_throws_error_if_bonus_pay_per_CV_is_invalid() {
        Job job = Job.builder()
                .client(Client.builder().name(randomString(10, 50)).build())
                .name(randomString(10, 50))
                .status(JobStatus.OPEN)
                .wantedCvs(randomInteger(1, 10))
                .contractType(ContractType.PERMANENT)
                .experienceRangeMin(randomInteger(0, 50))
                .experienceRangeMax(randomInteger(0, 50))
                .noticePeriodInDays(randomInteger(1, 20))
                .salaryBudget(1500.00)
                .currency(Currency.INR)
                .description(randomString(10, 100))
                .bonusPayPerCv(-1.0)
                .closureBonus(randomString(1, 100))
                .closureBonusPaymentDate(LocalDateTime.now())
                .cvRatePaymentDate(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> {
            repository.save(job);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Bonus pay per CV cannot be negative");
    }

    @Test
    public void save_throws_error_if_closure_bonus_is_invalid() {
        Job job = Job.builder()
                .client(Client.builder().name(randomString(10, 50)).build())
                .name(randomString(10, 50))
                .status(JobStatus.OPEN)
                .wantedCvs(randomInteger(1, 10))
                .contractType(ContractType.PERMANENT)
                .experienceRangeMin(randomInteger(0, 50))
                .experienceRangeMax(randomInteger(0, 50))
                .noticePeriodInDays(randomInteger(1, 20))
                .salaryBudget(1500.00)
                .currency(Currency.INR)
                .description(randomString(10, 100))
                .bonusPayPerCv(randomDouble(1, 1000))
                .closureBonus("")
                .closureBonusPaymentDate(LocalDateTime.now())
                .cvRatePaymentDate(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> {
            repository.save(job);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Closure bonus name must be between 1 and 255 characters long");
    }
}

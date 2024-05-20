package dev.lucavassos.recruiter.modules.job.domain;

import dev.lucavassos.recruiter.modules.client.repository.dto.ClientDto;
import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDto;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDto;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

public record NewJobRequest (
        @NotNull(message = "Client cannot be empty") ClientDto client,

        @NotBlank(message = "Name cannot be empty") String name,

        @NotNull(message = "Status cannot be empty") JobStatus status,

        @NotNull(message = "Contracy type cannot be empty") ContractType contractType,

        @NotNull(message = "Wanted CVs cannot be empty")
        @Min(value = 1, message = "Wanted CVs number must be greater than 0")
        Integer wantedCvs,

        @NotNull(message = "Experience range minimum cannot be empty")
        @Min(value = 0, message = "Experience range minimum must be a positive number")
        @Max(value = 50, message = "Experience range minimum must be less than 51 years")
        Integer experienceRangeMin,

        @NotNull(message = "Experience range maximum cannot be empty")
        @Min(value = 0, message = "Experience range maximum must be a positive number")
        @Max(value = 50, message = "Experience range maximum must be less than 51 years")
        Integer experienceRangeMax,

        @NotNull(message = "Notice period in days cannot be empty")
        @Min(value = 0, message = "Notice period in days must be a positive number")
        Integer noticePeriodInDays,

        List<SkillDto> skills,

        @NotNull(message = "Salary budget cannot be empty")
        @Min(value = 1, message = "Salary budget must be a greater than 0")
        Double salaryBudget,

        @NotNull(message = "Currency cannot be empty")
        Currency currency,

        @NotBlank(message = "Description cannot be empty")
        @Length(min = 1, max = 500, message = "Description must be between 1 and 500 characters")
        String description,

        @NotNull(message = "Bonus pay per CV cannot be empty")
        @Min(value = 1, message = "Bonus pay per CV must be greater than 0")
        Double bonusPayPerCv,

        @NotBlank(message = "Closure bonus cannot be empty") String closureBonus,

        @Future(message = "Closure bonus payment date must be in the future")
        LocalDateTime closureBonusPaymentDate,

        @Future(message = "CV rate payment date must be in the future")
        LocalDateTime cvRatePaymentDate,

        List<QuestionDto> questions
){}

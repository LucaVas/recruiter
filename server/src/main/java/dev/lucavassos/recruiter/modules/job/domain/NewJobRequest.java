package dev.lucavassos.recruiter.modules.job.domain;

import dev.lucavassos.recruiter.modules.job.repository.dto.ContractTypeDto;
import dev.lucavassos.recruiter.modules.skill.repository.dto.RawSkillDto;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record NewJobRequest (
        @NotBlank(message = "Client cannot be empty") String client,

        @NotBlank(message = "Name cannot be empty") String name,

        @NotNull(message = "Status cannot be empty") JobStatus status,

        @NotNull(message = "Contracy type cannot be empty") ContractTypeDto contractType,

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

        List<RawSkillDto> skills,

        @NotNull(message = "Salary budget cannot be empty")
        @Min(value = 1, message = "Salary budget must be a greater than 0")
        Double salaryBudget,

        @NotNull(message = "Currency cannot be empty")
        Currency currency,

        @NotBlank(message = "Description cannot be empty")
        String description,

        @NotNull(message = "Bonus pay per CV cannot be empty")
        @Min(value = 1, message = "Bonus pay per CV must be greater than 0")
        Double bonusPayPerCv,

        @NotNull(message = "Closure bonus cannot be empty")
        @Min(value = 1, message = "Closure bonus must be greater than 0")
        Double closureBonus,

        @Future(message = "Closure bonus payment date must be in the future")
        LocalDateTime closureBonusPaymentDate,

        @Future(message = "CV rate payment date must be in the future")
        LocalDateTime cvRatePaymentDate,

        String comments
){}

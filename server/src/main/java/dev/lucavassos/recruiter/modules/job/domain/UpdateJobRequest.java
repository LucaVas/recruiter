package dev.lucavassos.recruiter.modules.job.domain;

import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionnaireDto;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.Set;

public record UpdateJobRequest(
        @NotNull Long id,

        @Valid
        Client client,

        @NotBlank(message = "Job name is required")
        String name,

        @NotNull(message = "Job status is required")
        JobStatus status,

        @NotNull(message = "Job contract questionType is required")
        ContractType contractType,

        @NotNull(message = "Job wanted CVs number is required")
        @Min(value = 1, message = "Job wanted CVs number must be greater than 0")
        Integer wantedCvs,

        @NotNull(message = "Job experience range minimum is required")
        @Min(value = 0, message = "Job experience range minimum must be a positive number")
        @Max(value = 50, message = "Job experience range minimum must be less than 51 years")
        Integer experienceRangeMin,

        @NotNull(message = "Job experience range maximum is required")
        @Min(value = 0, message = "Job experience range maximum must be a positive number")
        @Max(value = 50, message = "Job experience range maximum must be less than 51 years")
        Integer experienceRangeMax,

        @NotNull(message = "Job notice period in days is required")
        @Min(value = 0, message = "Job notice period in days must be a positive number")
        Integer noticePeriodInDays,

        @Valid
        @NotEmpty(message = "Job must have at least one skill")
        Set<SkillDto> skills,

        @NotNull(message = "Job salary budget is required")
        @Min(value = 1, message = "Job salary budget must be a greater than 0")
        Double salaryBudget,

        @NotNull(message = "Job salary budget currency is required")
        Currency currency,

        @NotBlank(message = "Job description is required")
        @Length(max = 500, message = "Job description must be less than 500 characters")
        String description,

        @NotNull(message = "Job payment per CV upload is required")
        @Min(value = 1, message = "Job payment per CV upload must be greater than 0")
        Double bonusPayPerCv,

        @NotBlank(message = "Job candidate joining bonus is required")
        String closureBonus,

        @Future(message = "Job candidate joining bonus payment date must be in the future")
        LocalDateTime closureBonusPaymentDate,

        @Future(message = "Job payment date per CV upload must be in the future")
        LocalDateTime cvRatePaymentDate,

        @Valid
        QuestionnaireDto questionnaire
) {}

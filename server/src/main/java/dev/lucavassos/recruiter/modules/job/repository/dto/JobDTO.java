package dev.lucavassos.recruiter.modules.job.repository.dto;

import dev.lucavassos.recruiter.modules.client.repository.dto.ClientDto;
import dev.lucavassos.recruiter.modules.job.domain.ContractType;
import dev.lucavassos.recruiter.modules.job.domain.Currency;
import dev.lucavassos.recruiter.modules.job.domain.JobStatus;
import dev.lucavassos.recruiter.modules.questionnaire.repository.dto.QuestionnaireDto;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobDTO {

    @NotNull
    private Long id;

    @Valid
    private ClientDto client;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Status cannot be empty")
    private JobStatus status;

    @NotNull(message = "Wanted CVs cannot be empty")
    @Min(value = 1, message = "Wanted CVs number must be greater than 0")
    private Integer wantedCvs;

    @NotNull(message = "Contract questionType cannot be empty")
    private ContractType contractType;

    @NotNull(message = "Experience range minimum cannot be empty")
    @Min(value = 0, message = "Experience range minimum must be a positive number")
    @Max(value = 50, message = "Experience range minimum must be less than 51 years")
    private Integer experienceRangeMin;

    @NotNull(message = "Experience range maximum cannot be empty")
    @Min(value = 0, message = "Experience range maximum must be a positive number")
    @Max(value = 50, message = "Experience range maximum must be less than 51 years")
    private Integer experienceRangeMax;

    @NotNull(message = "Notice period in days cannot be empty")
    @Min(value = 0, message = "Notice period in days must be a positive number")
    private Integer noticePeriodInDays;

    @NotNull(message = "Salary budget cannot be empty")
    @Min(value = 1, message = "Salary budget must be a greater than 0")
    private Double salaryBudget;

    @NotNull(message = "Currency cannot be empty")
    private Currency currency;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Bonus pay per CV cannot be empty")
    @Min(value = 1, message = "Bonus pay per CV must be greater than 0")
    private Double bonusPayPerCv;

    @NotBlank(message = "Closure bonus cannot be empty")
    private String closureBonus;

    @Future(message = "Closure bonus payment date must be in the future")
    private LocalDateTime closureBonusPaymentDate;

    @Future(message = "CV rate payment date must be in the future")
    private LocalDateTime cvRatePaymentDate;

    @NotNull(message = "Number of candidates cannot be empty")
    @Min(value = 0, message = "Number of candidates must be a positive number")
    private Integer numberOfCandidates;

    List<SkillDto> skills;

    @Valid
    private QuestionnaireDto questionnaire;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

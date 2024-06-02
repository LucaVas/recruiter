package dev.lucavassos.recruiter.modules.questionnaire.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
@Embeddable
public class QuestionnaireId implements Serializable {

    @Column(nullable = false, name = "title")
    @Size(min = 1, message = "Question title must be at least 1 character long")
    private String title;

    @Column(name = "client_name")
    private String clientName;

}

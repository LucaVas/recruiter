package dev.lucavassos.recruiter.modules.client.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record NewClientRequest(

        @NotBlank(message = "Client name cannot be empty")
        @Length(max = 255, message = "Client name cannot be longer than 255 characters")
        String name,

        @NotNull(message = "Client industry cannot be empty") Industry industry
) {
}

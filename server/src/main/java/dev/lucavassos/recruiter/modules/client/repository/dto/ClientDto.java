package dev.lucavassos.recruiter.modules.client.repository.dto;

import dev.lucavassos.recruiter.modules.client.domain.Industry;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClientDto {

    @NotBlank(message = "Client name is required")
    private String name;

    @NotNull(message = "Client industry is required")
    private Industry industry;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

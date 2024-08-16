package dev.lucavassos.recruiter.modules.job.domain;

import dev.lucavassos.recruiter.modules.CustomPage;
import dev.lucavassos.recruiter.modules.job.repository.dto.JobDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString
@Builder
@Data
public class PaginatedJobsResponse extends CustomPage<JobDto> {
    List<JobDto> elements;
    Integer page;
    Integer totalPages;
    Long totalElements;
}

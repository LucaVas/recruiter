package dev.lucavassos.recruiter.modules.user.domain;

import dev.lucavassos.recruiter.modules.CustomPage;
import dev.lucavassos.recruiter.modules.user.repository.dto.UserDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString
@Builder
@Data
public class PaginatedUsersResponse extends CustomPage<UserDto> {
    List<UserDto> elements;
    Integer page;
    Integer totalPages;
    Long totalElements;
}

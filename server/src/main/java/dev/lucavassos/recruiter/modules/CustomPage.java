package dev.lucavassos.recruiter.modules;

import dev.lucavassos.recruiter.modules.user.repository.dto.UserDto;

import java.util.List;

public abstract class CustomPage<T> {
    List<T> users;
    Integer page;
    Integer totalPages;
    Long totalElements;
}

package dev.lucavassos.recruiter.modules;

import java.util.List;

public abstract class CustomPage<T> {
    List<T> elements;
    Integer page;
    Integer totalPages;
    Long totalElements;
}

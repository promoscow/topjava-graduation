package ru.xpendence.topjavagraduation.controller.mapper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PageableMapper {

    private final static int PAGE_DEFAULT = 0;
    private final static int SIZE_DEFAULT = 10;

    public Pageable toPageable(Integer page, Integer size) {
        if (Objects.isNull(page)) page = PAGE_DEFAULT;
        if (Objects.isNull(size)) size = SIZE_DEFAULT;
        return PageRequest.of(page, size);
    }
}

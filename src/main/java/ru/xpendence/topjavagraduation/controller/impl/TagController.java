package ru.xpendence.topjavagraduation.controller.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.xpendence.topjavagraduation.controller.mapper.PageableMapper;
import ru.xpendence.topjavagraduation.controller.mapper.TagModelMapper;
import ru.xpendence.topjavagraduation.controller.model.request.TagCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.TagUpdateRequest;
import ru.xpendence.topjavagraduation.controller.model.response.TagResponse;
import ru.xpendence.topjavagraduation.service.TagService;

@RestController
@RequestMapping("/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Теги")
public class TagController {

    private final TagService service;
    private final PageableMapper pageableMapper;

    public TagController(TagService service, PageableMapper pageableMapper) {
        this.service = service;
        this.pageableMapper = pageableMapper;
    }

    @PostMapping
    @Operation(summary = "Создание тега")
    public TagResponse create(
            @Parameter(description = "Запрос на создание тега")
            @RequestBody
            @Validated
            TagCreateRequest request
    ) {
        return TagModelMapper.toResponse(service.create(TagModelMapper.toTag(request)));
    }

    @PutMapping
    @Operation(summary = "Обновление тега")
    public HttpStatus update(
            @Parameter(description = "Запрос на обновление тега")
            @RequestBody
            @Validated
            TagUpdateRequest request
    ) {
        service.update(TagModelMapper.toTag(request));
        return HttpStatus.OK;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение тега")
    public TagResponse get(
            @Parameter(description = "Идентификатор тега")
            @PathVariable Long id
    ) {
        return TagModelMapper.toResponse(service.getById(id));
    }

    @GetMapping("/all")
    @Operation(summary = "Получение тегов с фильтрами и пагинацией")
    public Page<TagResponse> getAll(
            @Parameter(description = "Фильтр по названию тега")
            @RequestParam(required = false)
            String name,

            @Parameter(description = "Номер страницы")
            @RequestParam(required = false)
            Integer page,

            @Parameter(description = "Размер страницы")
            @RequestParam(required = false)
            Integer size
    ) {
        return service.getAll(name, pageableMapper.toPageable(page, size))
                .map(TagModelMapper::toResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление тега")
    public HttpStatus delete(
            @Parameter(description = "Идентификатор тега")
            @PathVariable Long id
    ) {
        service.delete(id);
        return HttpStatus.OK;
    }
}

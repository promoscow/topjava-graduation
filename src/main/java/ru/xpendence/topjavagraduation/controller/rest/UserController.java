package ru.xpendence.topjavagraduation.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.xpendence.topjavagraduation.controller.mapper.PageableMapper;
import ru.xpendence.topjavagraduation.controller.mapper.UserModelMapper;
import ru.xpendence.topjavagraduation.controller.model.request.UserCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.UserUpdateRequest;
import ru.xpendence.topjavagraduation.controller.model.response.UserResponse;
import ru.xpendence.topjavagraduation.service.UserService;

@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи")
public class UserController {

    private final UserService service;
    private final PageableMapper pageableMapper;

    public UserController(UserService service, PageableMapper pageableMapper) {
        this.service = service;
        this.pageableMapper = pageableMapper;
    }

    @PostMapping
    @Operation(summary = "Регистрация пользователя")
    public UserResponse create(
            @Parameter(description = "Запрос на регистрацию пользователя")
            @RequestBody
            @Validated
            UserCreateRequest request
    ) {
        return UserModelMapper.toResponse(service.create(UserModelMapper.toUser(request)));
    }

    @PutMapping
    @Operation(summary = "Обновление пользователя")
    public HttpStatus update(
            @Parameter(description = "Запрос на обновление пользователя")
            @RequestBody
            @Validated
            UserUpdateRequest request
    ) {
        service.update(UserModelMapper.toUser(request));
        return HttpStatus.OK;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя")
    public UserResponse get(
            @Parameter(description = "Идентификатор пользователя")
            @PathVariable Long id
    ) {
        return UserModelMapper.toResponse(service.getById(id));
    }

    @GetMapping("/all")
    @Operation(summary = "Получение пользователей с фильтрами и пагинацией")
    public Page<UserResponse> getAll(
            @Parameter(description = "Фильтр по имени пользователя")
            @RequestParam(required = false)
            String username,

            @Parameter(description = "Номер страницы")
            @RequestParam(required = false)
            Integer page,

            @Parameter(description = "Размер страницы")
            @RequestParam(required = false)
            Integer size
    ) {
        return service.getAll(username, pageableMapper.toPageable(page, size))
                .map(UserModelMapper::toResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя")
    public HttpStatus delete(
            @Parameter(description = "Идентификатор пользователя")
            @PathVariable Long id
    ) {
        service.delete(id);
        return HttpStatus.OK;
    }
}

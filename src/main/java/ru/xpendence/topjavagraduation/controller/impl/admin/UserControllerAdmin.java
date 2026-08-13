package ru.xpendence.topjavagraduation.controller.impl.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import ru.xpendence.topjavagraduation.controller.mapper.UserModelMapper;
import ru.xpendence.topjavagraduation.controller.model.request.UserCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.UserUpdateRequest;
import ru.xpendence.topjavagraduation.controller.model.response.UserResponse;
import ru.xpendence.topjavagraduation.service.UserService;

@RestController
@RequestMapping("/admin/users")
@Tag(name = "Пользователи")
public class UserControllerAdmin {

    private final UserService service;
    private final PageableMapper pageableMapper;

    public UserControllerAdmin(UserService service, PageableMapper pageableMapper) {
        this.service = service;
        this.pageableMapper = pageableMapper;
    }

    @PostMapping
    @Operation(summary = "Создание пользователя")
    public UserResponse create(
            @Parameter(description = "Запрос на создание пользователя")
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
    @Operation(summary = "Получение пользователя по ID")
    public UserResponse get(
            @Parameter(description = "Идентификатор пользователя")
            @PathVariable
            Long id
    ) {
        return UserModelMapper.toResponse(service.getById(id));
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Получение пользователя по имени")
    public UserResponse getByUsername(
            @Parameter(description = "Имя пользователя")
            @PathVariable
            String username
    ) {
        return UserModelMapper.toResponse(service.getByUsername(username));
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

    @PutMapping("/{id}/roles/{roleId}")
    @Operation(summary = "Добавление роли пользователю")
    public HttpStatus addRole(
            @Parameter(description = "Идентификатор пользователя")
            @PathVariable
            Long id,

            @Parameter(description = "Идентификатор роли")
            @PathVariable
            Long roleId
    ) {
        service.addRole(id, roleId);
        return HttpStatus.OK;
    }

    @DeleteMapping("/{id}/roles/{roleId}")
    @Operation(summary = "Удаление роли у пользователя")
    public HttpStatus removeRole(
            @Parameter(description = "Идентификатор пользователя")
            @PathVariable
            Long id,

            @Parameter(description = "Идентификатор роли")
            @PathVariable
            Long roleId
    ) {
        service.removeRole(id, roleId);
        return HttpStatus.OK;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя")
    public HttpStatus delete(
            @Parameter(description = "Идентификатор пользователя")
            @PathVariable
            Long id
    ) {
        service.delete(id);
        return HttpStatus.OK;
    }
}

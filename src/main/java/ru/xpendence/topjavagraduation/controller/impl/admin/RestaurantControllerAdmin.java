package ru.xpendence.topjavagraduation.controller.impl.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.xpendence.topjavagraduation.controller.Validation;
import ru.xpendence.topjavagraduation.controller.mapper.PageableMapper;
import ru.xpendence.topjavagraduation.controller.mapper.RestaurantModelMapper;
import ru.xpendence.topjavagraduation.controller.model.request.RestaurantRequest;
import ru.xpendence.topjavagraduation.controller.model.response.RestaurantResponse;
import ru.xpendence.topjavagraduation.service.RestaurantService;

@RestController
@RequestMapping("/admin/restaurants")
@Tag(name = "Рестораны")
public class RestaurantControllerAdmin {

    private final RestaurantService service;
    private final RestaurantModelMapper mapper;
    private final PageableMapper pageableMapper;

    public RestaurantControllerAdmin(
            RestaurantService service,
            RestaurantModelMapper mapper,
            PageableMapper pageableMapper) {
        this.service = service;
        this.mapper = mapper;
        this.pageableMapper = pageableMapper;
    }

    @Operation(summary = "Создание нового ресторана")
    @PostMapping
    public RestaurantResponse create(
            @Parameter(description = "Запрос на создание нового ресторана")
            @RequestBody
            @Validated(Validation.Create.class)
            RestaurantRequest request
    ) {
        return mapper.toResponse(service.create(mapper.toRestaurantForCreate(request)));
    }

    @Operation(summary = "Обновление ресторана")
    @PutMapping
    public HttpStatus update(
            @Parameter(description = "Запрос на обновление ресторана")
            @RequestBody
            @Validated(Validation.Update.class) RestaurantRequest request
    ) {
        service.update(mapper.toRestaurantForUpdate(request));
        return HttpStatus.OK;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение ресторана")
    public RestaurantResponse get(
            @Parameter(description = "Идентификатор ресторана")
            @PathVariable Long id
    ) {
        return mapper.toResponse(service.getById(id));
    }

    @Operation(summary = "Получение всех ресторанов")
    @GetMapping("/all")
    public Page<RestaurantResponse> getAll(
            @Parameter(description = "Номер страницы")
            @RequestParam(required = false)
            Integer page,

            @Parameter(description = "Размер страницы")
            @RequestParam(required = false)
            Integer size) {
        return service.getAll(pageableMapper.toPageable(page, size)).map(mapper::toResponse);
    }
}

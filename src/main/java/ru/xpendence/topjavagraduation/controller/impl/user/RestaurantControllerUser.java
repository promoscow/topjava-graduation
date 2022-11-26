package ru.xpendence.topjavagraduation.controller.impl.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.xpendence.topjavagraduation.controller.mapper.PageableMapper;
import ru.xpendence.topjavagraduation.controller.mapper.RestaurantModelMapper;
import ru.xpendence.topjavagraduation.controller.model.response.RestaurantResponse;
import ru.xpendence.topjavagraduation.service.RestaurantService;

@RestController
@RequestMapping("/user/restaurants")
@Tag(name = "Рестораны")
public class RestaurantControllerUser {

    private final RestaurantService service;
    private final RestaurantModelMapper mapper;
    private final PageableMapper pageableMapper;

    public RestaurantControllerUser(
            RestaurantService service,
            RestaurantModelMapper mapper,
            PageableMapper pageableMapper) {
        this.service = service;
        this.mapper = mapper;
        this.pageableMapper = pageableMapper;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение ресторана")
    public RestaurantResponse get(
            @Parameter(description = "Идентификатор ресторана")
            @PathVariable Long id
    ) {
        return mapper.toResponse(service.getById(id));
    }

    @GetMapping("/chosen")
    @Operation(summary = "Получение выбранного ресторана")
    public RestaurantResponse getChosen() {
        return mapper.toResponse(service.getChosen());
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

package ru.xpendence.topjavagraduation.controller.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.xpendence.topjavagraduation.controller.RestaurantController;
import ru.xpendence.topjavagraduation.controller.mapper.PageableMapper;
import ru.xpendence.topjavagraduation.controller.mapper.RestaurantModelMapper;
import ru.xpendence.topjavagraduation.controller.model.request.RestaurantRequest;
import ru.xpendence.topjavagraduation.controller.model.response.RestaurantResponse;
import ru.xpendence.topjavagraduation.service.RestaurantService;

@RestController
@RequestMapping("/user/restaurants")
@Tag(name = "Рестораны")
public class RestaurantControllerImpl implements RestaurantController {

    private final RestaurantService service;
    private final RestaurantModelMapper mapper;
    private final PageableMapper pageableMapper;

    public RestaurantControllerImpl(
            RestaurantService service,
            RestaurantModelMapper mapper,
            PageableMapper pageableMapper) {
        this.service = service;
        this.mapper = mapper;
        this.pageableMapper = pageableMapper;
    }

    @Override
    @Operation(summary = "Создание нового ресторана")
    @PostMapping
    public RestaurantResponse create(@RequestBody RestaurantRequest request) {
        return mapper.toResponse(service.create(mapper.toRestaurantForCreate(request)));
    }

    @Override
    @Operation(summary = "Обновление ресторана")
    @PutMapping
    public HttpStatus update(RestaurantRequest request) {
        service.update(mapper.toRestaurantForUpdate(request));
        return HttpStatus.OK;
    }

    @Override
    @GetMapping("/{id}")
    @Operation(summary = "Получение ресторана")
    public RestaurantResponse get(@PathVariable Long id) {
        return mapper.toResponse(service.getById(id));
    }

    @Override
    @GetMapping("/chosen")
    @Operation(summary = "Получение выбранного ресторана")
    public RestaurantResponse getChosen() {
        return mapper.toResponse(service.getChosen());
    }

    @Override
    @Operation(summary = "Получение всех ресторанов")
    @GetMapping("/all")
    public Page<RestaurantResponse> getAll(
            @RequestParam(required = false)
            Integer page,

            @RequestParam(required = false)
            Integer size) {
        return service.getAll(pageableMapper.toPageable(page, size)).map(mapper::toResponse);
    }


}

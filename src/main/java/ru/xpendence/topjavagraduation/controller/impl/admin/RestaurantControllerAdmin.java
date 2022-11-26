package ru.xpendence.topjavagraduation.controller.impl.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    public RestaurantResponse create(@RequestBody RestaurantRequest request) {
        return mapper.toResponse(service.create(mapper.toRestaurantForCreate(request)));
    }

    @Operation(summary = "Обновление ресторана")
    @PutMapping
    public HttpStatus update(RestaurantRequest request) {
        service.update(mapper.toRestaurantForUpdate(request));
        return HttpStatus.OK;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение ресторана")
    public RestaurantResponse get(@PathVariable Long id) {
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
            @RequestParam(required = false)
            Integer page,

            @RequestParam(required = false)
            Integer size) {
        return service.getAll(pageableMapper.toPageable(page, size)).map(mapper::toResponse);
    }
}

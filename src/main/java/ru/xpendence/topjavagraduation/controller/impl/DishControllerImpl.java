package ru.xpendence.topjavagraduation.controller.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.xpendence.topjavagraduation.controller.DishController;
import ru.xpendence.topjavagraduation.controller.mapper.DishModelMapper;
import ru.xpendence.topjavagraduation.controller.mapper.PageableMapper;
import ru.xpendence.topjavagraduation.controller.model.request.DishCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.DishUpdateRequest;
import ru.xpendence.topjavagraduation.controller.model.response.DishResponse;
import ru.xpendence.topjavagraduation.service.DishService;

@RestController
@RequestMapping("/dishes")
// TODO: 13.11.2022 валидация!!!
// TODO: 13.11.2022 ExceptionHandler!!!
@Tag(name = "Блюда")
public class DishControllerImpl implements DishController {

    private final DishService service;
    private final DishModelMapper mapper;
    private final PageableMapper pageableMapper;

    public DishControllerImpl(DishService service, DishModelMapper mapper, PageableMapper pageableMapper) {
        this.service = service;
        this.mapper = mapper;
        this.pageableMapper = pageableMapper;
    }

    @Override
    @PostMapping
    @Operation(summary = "Создание блюда")
    public DishResponse create(@RequestBody DishCreateRequest request) {
        return mapper.toResponse(service.create(mapper.toDish(request)));
    }

    @Override
    @PutMapping
    @Operation(summary = "Обновление блюда")
    public HttpStatus update(@RequestBody DishUpdateRequest request) {
        service.update(mapper.toDish(request));
        return HttpStatus.OK;
    }

    @Override
    @GetMapping("/{id}")
    @Operation(summary = "Получение блюда по ID")
    public DishResponse get(@PathVariable Long id) {
        return mapper.toResponse(service.getById(id));
    }

    @Override
    @GetMapping("/all/restaurant/{restaurantId}")
    @Operation(summary = "Получение блюд ресторана по ID ресторана")
    public Page<DishResponse> getAllByRestaurantId(
            @PathVariable
            Long restaurantId,

            @RequestParam(required = false)
            Integer page,

            @RequestParam(required = false)
            Integer size
    ) {
        return service.getAllByRestaurantId(restaurantId, pageableMapper.toPageable(page, size))
                .map(mapper::toResponse);
    }
}
package ru.xpendence.topjavagraduation.controller.impl.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.xpendence.topjavagraduation.controller.mapper.DishModelMapper;
import ru.xpendence.topjavagraduation.controller.mapper.PageableMapper;
import ru.xpendence.topjavagraduation.controller.model.request.DishCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.DishUpdateRequest;
import ru.xpendence.topjavagraduation.controller.model.response.DishResponse;
import ru.xpendence.topjavagraduation.service.DishService;

@RestController
@RequestMapping("/admin/dishes")
@Tag(name = "Блюда")
public class DishControllerAdmin {

    private final DishService service;
    private final DishModelMapper mapper;
    private final PageableMapper pageableMapper;

    public DishControllerAdmin(DishService service, DishModelMapper mapper, PageableMapper pageableMapper) {
        this.service = service;
        this.mapper = mapper;
        this.pageableMapper = pageableMapper;
    }

    @PostMapping
    @Operation(summary = "Создание блюда")
    public DishResponse create(
            @Parameter(description = "Запрос на добавление блюда")
            @RequestBody
            @Validated
            DishCreateRequest request
    ) {
        return mapper.toResponse(service.create(mapper.toDish(request)));
    }

    @PutMapping
    @Operation(summary = "Обновление блюда")
    public HttpStatus update(
            @Parameter(description = "Запрос на обновление блюда")
            @RequestBody
            @Validated
            DishUpdateRequest request
    ) {
        service.update(mapper.toDish(request));
        return HttpStatus.OK;
    }

    @PutMapping("/reset/restaurant/{restaurantId}")
    @Operation(summary = "Сброс меню для ресторана")
    public HttpStatus resetMenu(
            @Parameter(description = "ID ресторана")
            @PathVariable
            Long restaurantId
    ) {
        service.resetMenu(restaurantId);
        return HttpStatus.OK;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение блюда по ID")
    public DishResponse get(
            @Parameter(description = "ID блюда")
            @PathVariable
            Long id
    ) {
        return mapper.toResponse(service.getById(id));
    }

    @GetMapping("/all/restaurant/{restaurantId}")
    @Operation(summary = "Получение блюд ресторана по ID ресторана")
    public Page<DishResponse> getAllByRestaurantId(
            @Parameter(description = "ID ресторана")
            @PathVariable
            Long restaurantId,

            @Parameter(description = "Номер страницы")
            @RequestParam(required = false)
            Integer page,

            @Parameter(description = "Размер страницы")
            @RequestParam(required = false)
            Integer size
    ) {
        return service.getAllByRestaurantId(restaurantId, pageableMapper.toPageable(page, size))
                .map(mapper::toResponse);
    }
}

package ru.xpendence.topjavagraduation.controller.impl.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.xpendence.topjavagraduation.controller.mapper.DishModelMapper;
import ru.xpendence.topjavagraduation.controller.mapper.PageableMapper;
import ru.xpendence.topjavagraduation.controller.model.response.DishResponse;
import ru.xpendence.topjavagraduation.service.DishService;

@RestController
@RequestMapping("/user/dishes")
@Tag(name = "Блюда")
public class DishControllerUser {

    private final DishService service;
    private final DishModelMapper mapper;
    private final PageableMapper pageableMapper;

    public DishControllerUser(DishService service, DishModelMapper mapper, PageableMapper pageableMapper) {
        this.service = service;
        this.mapper = mapper;
        this.pageableMapper = pageableMapper;
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

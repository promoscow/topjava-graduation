package ru.xpendence.topjavagraduation.controller.rest.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.xpendence.topjavagraduation.controller.mapper.PageableMapper;
import ru.xpendence.topjavagraduation.controller.mapper.ReviewModelMapper;
import ru.xpendence.topjavagraduation.controller.model.response.ReviewResponse;
import ru.xpendence.topjavagraduation.service.ReviewService;

@RestController
@RequestMapping("/admin/reviews")
@Tag(name = "Отзывы")
public class ReviewControllerAdmin {

    private final ReviewService service;
    private final ReviewModelMapper mapper;
    private final PageableMapper pageableMapper;

    public ReviewControllerAdmin(ReviewService service, ReviewModelMapper mapper, PageableMapper pageableMapper) {
        this.service = service;
        this.mapper = mapper;
        this.pageableMapper = pageableMapper;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение отзыва по ID")
    public ReviewResponse get(
            @Parameter(description = "Идентификатор отзыва")
            @PathVariable Long id
    ) {
        return mapper.toResponse(service.getById(id));
    }

    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Получение отзывов ресторана")
    public Page<ReviewResponse> getAllByRestaurantId(
            @Parameter(description = "Идентификатор ресторана")
            @PathVariable Long restaurantId,

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

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление отзыва")
    public HttpStatus delete(
            @Parameter(description = "Идентификатор отзыва")
            @PathVariable Long id
    ) {
        service.delete(id);
        return HttpStatus.OK;
    }
}

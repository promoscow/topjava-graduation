package ru.xpendence.topjavagraduation.controller.impl.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.xpendence.topjavagraduation.config.security.model.JwtUser;
import ru.xpendence.topjavagraduation.controller.mapper.PageableMapper;
import ru.xpendence.topjavagraduation.controller.mapper.ReviewModelMapper;
import ru.xpendence.topjavagraduation.controller.model.request.ReviewCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.ReviewUpdateRequest;
import ru.xpendence.topjavagraduation.controller.model.response.ReviewResponse;
import ru.xpendence.topjavagraduation.service.ReviewService;

@RestController
@RequestMapping("/user/reviews")
@Tag(name = "Отзывы")
public class ReviewControllerUser {

    private final ReviewService service;
    private final ReviewModelMapper mapper;
    private final PageableMapper pageableMapper;

    public ReviewControllerUser(ReviewService service, ReviewModelMapper mapper, PageableMapper pageableMapper) {
        this.service = service;
        this.mapper = mapper;
        this.pageableMapper = pageableMapper;
    }

    @PostMapping
    @Operation(summary = "Создание отзыва")
    public ReviewResponse create(
            @Parameter(description = "Запрос на создание отзыва")
            @Validated
            @RequestBody ReviewCreateRequest request,

            @AuthenticationPrincipal JwtUser jwtUser
    ) {
        return mapper.toResponse(service.create(mapper.toReview(request, jwtUser.getId())));
    }

    @PutMapping
    @Operation(summary = "Обновление отзыва")
    public HttpStatus update(
            @Parameter(description = "Запрос на обновление отзыва")
            @Validated
            @RequestBody ReviewUpdateRequest request,

            @AuthenticationPrincipal JwtUser jwtUser
    ) {
        service.update(mapper.toReview(request), jwtUser.getId());
        return HttpStatus.OK;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение отзыва по ID")
    public ReviewResponse get(
            @Parameter(description = "Идентификатор отзыва")
            @PathVariable Long id
    ) {
        return mapper.toResponse(service.getById(id));
    }

    @GetMapping("/me/restaurant/{restaurantId}")
    @Operation(summary = "Получение отзыва текущего пользователя по ресторану")
    public ReviewResponse getByUserIdAndRestaurantId(
            @Parameter(description = "Идентификатор ресторана")
            @PathVariable Long restaurantId,

            @AuthenticationPrincipal JwtUser jwtUser
    ) {
        return mapper.toResponse(service.getByUserIdAndRestaurantId(jwtUser.getId(), restaurantId));
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
}

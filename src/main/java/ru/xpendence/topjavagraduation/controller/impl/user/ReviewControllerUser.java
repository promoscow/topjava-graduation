package ru.xpendence.topjavagraduation.controller.impl.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
            @RequestBody ReviewCreateRequest request
    ) {
        return mapper.toResponse(service.create(mapper.toReview(request)));
    }

    @PutMapping
    @Operation(summary = "Обновление отзыва")
    public HttpStatus update(
            @Parameter(description = "Запрос на обновление отзыва")
            @Validated
            @RequestBody ReviewUpdateRequest request
    ) {
        service.update(mapper.toReview(request));
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

    @GetMapping("/user/{userId}/restaurant/{restaurantId}")
    @Operation(summary = "Получение отзыва пользователя по ресторану")
    public ReviewResponse getByUserIdAndRestaurantId(
            @Parameter(description = "Идентификатор пользователя")
            @PathVariable Long userId,

            @Parameter(description = "Идентификатор ресторана")
            @PathVariable Long restaurantId
    ) {
        return mapper.toResponse(service.getByUserIdAndRestaurantId(userId, restaurantId));
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

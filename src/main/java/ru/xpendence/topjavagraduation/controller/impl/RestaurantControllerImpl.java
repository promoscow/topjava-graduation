package ru.xpendence.topjavagraduation.controller.impl;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.xpendence.topjavagraduation.controller.RestaurantController;
import ru.xpendence.topjavagraduation.controller.mapper.PageableMapper;
import ru.xpendence.topjavagraduation.controller.mapper.RestaurantModelMapper;
import ru.xpendence.topjavagraduation.controller.model.request.RestaurantRequest;
import ru.xpendence.topjavagraduation.controller.model.response.RestaurantResponse;
import ru.xpendence.topjavagraduation.service.RestaurantService;

@RestController
@RequestMapping("/restaurants")
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
    @PostMapping
    public RestaurantResponse create(@RequestBody RestaurantRequest request) {
        return mapper.toResponse(service.create(mapper.toRestaurant(request)));
    }

    @Override
    @GetMapping("/all")
    public Page<RestaurantResponse> getAll(
            @RequestParam(required = false)
            Integer page,

            @RequestParam(required = false)
            Integer size) {
        return service.getAll(pageableMapper.toPageable(page, size)).map(mapper::toResponse);
    }
}

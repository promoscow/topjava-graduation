package ru.xpendence.topjavagraduation.controller.mapper;

import org.springframework.stereotype.Component;
import ru.xpendence.topjavagraduation.controller.model.request.VoteRequest;
import ru.xpendence.topjavagraduation.controller.model.response.VoteResponse;
import ru.xpendence.topjavagraduation.entity.Vote;
import ru.xpendence.topjavagraduation.service.RestaurantService;
import ru.xpendence.topjavagraduation.service.UserService;

import java.time.LocalDate;

@Component
public class VoteMapper {

    private final UserService userService;
    private final RestaurantService restaurantService;

    public VoteMapper(UserService userService, RestaurantService restaurantService) {
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    public Vote toVote(VoteRequest request) {
        var vote = new Vote();
        vote.setDate(LocalDate.now());
        vote.setUser(userService.getById(request.userId()));
        vote.setRestaurant(restaurantService.getById(request.restaurantId()));
        return vote;
    }

    public VoteResponse toResponse(Vote vote) {
        return new VoteResponse(
                vote.getId(),
                vote.getDate(),
                vote.getUser().getId(),
                vote.getRestaurant().getId()
        );
    }
}

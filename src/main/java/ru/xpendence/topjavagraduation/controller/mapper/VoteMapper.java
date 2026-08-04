package ru.xpendence.topjavagraduation.controller.mapper;

import org.springframework.stereotype.Component;
import ru.xpendence.topjavagraduation.controller.model.request.VoteRequest;
import ru.xpendence.topjavagraduation.controller.model.response.VoteResponse;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.entity.User;
import ru.xpendence.topjavagraduation.entity.Vote;

import java.time.LocalDate;

@Component
public class VoteMapper {

    public Vote toVote(VoteRequest request) {
        var vote = new Vote();
        vote.setDate(LocalDate.now());
        var user = new User();
        user.setId(request.userId());
        vote.setUser(user);
        var restaurant = new Restaurant();
        restaurant.setId(request.restaurantId());
        vote.setRestaurant(restaurant);
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

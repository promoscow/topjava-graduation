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

    public Vote toVote(VoteRequest request, Long userId) {
        return new Vote(
                null,
                LocalDate.now(),
                new User(userId, "", ""),
                new Restaurant(request.restaurantId(), null)
        );
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

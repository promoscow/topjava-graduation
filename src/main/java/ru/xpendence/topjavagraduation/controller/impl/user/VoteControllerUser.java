package ru.xpendence.topjavagraduation.controller.impl.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.xpendence.topjavagraduation.controller.mapper.VoteMapper;
import ru.xpendence.topjavagraduation.controller.model.request.VoteRequest;
import ru.xpendence.topjavagraduation.controller.model.response.VoteResponse;
import ru.xpendence.topjavagraduation.service.VoteService;

@RestController
@RequestMapping("/user/votes")
@Tag(name = "Голосование")
public class VoteControllerUser {

    private final VoteService service;
    private final VoteMapper mapper;

    public VoteControllerUser(VoteService service, VoteMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public VoteResponse vote(
            @Validated
            @RequestBody VoteRequest request
    ) {
        return mapper.toResponse(service.create(mapper.toVote(request)));
    }
}

package ru.xpendence.topjavagraduation.controller.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.xpendence.topjavagraduation.controller.VoteController;
import ru.xpendence.topjavagraduation.controller.mapper.VoteMapper;
import ru.xpendence.topjavagraduation.controller.model.request.VoteRequest;
import ru.xpendence.topjavagraduation.controller.model.response.VoteSimpleResponse;
import ru.xpendence.topjavagraduation.service.VoteService;

@RestController
@RequestMapping("votes")
@Tag(name = "Голосование")
public class VoteControllerImpl implements VoteController {

    private final VoteService service;
    private final VoteMapper mapper;

    public VoteControllerImpl(VoteService service, VoteMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    @PostMapping
    public VoteSimpleResponse vote(@RequestBody VoteRequest request) {
        return mapper.toResponse(service.create(mapper.toVote(request)));
    }
}

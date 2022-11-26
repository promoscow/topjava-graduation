package ru.xpendence.topjavagraduation.controller.impl.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
    @Operation(summary = "Голосование пользователем")
    public VoteResponse vote(
            @Parameter(description = "Запрос на голосование")
            @Validated
            @RequestBody VoteRequest request
    ) {
        return mapper.toResponse(service.create(mapper.toVote(request)));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получение текущего голоса пользователя")
    public VoteResponse getByUserId(
            @Parameter(description = "Идентификатор пользователя")
            @PathVariable Long userId
    ) {
        return mapper.toResponse(service.getByUserId(userId));
    }
}

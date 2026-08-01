package ru.xpendence.topjavagraduation.controller.impl.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.xpendence.topjavagraduation.controller.mapper.VoteMapper;
import ru.xpendence.topjavagraduation.controller.model.request.VoteRequest;
import ru.xpendence.topjavagraduation.controller.model.response.VoteResponse;
import ru.xpendence.topjavagraduation.service.VoteService;

import java.time.LocalDate;
import java.util.Objects;

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
    @Operation(summary = "Получение голоса пользователя за указанную дату")
    public VoteResponse getByUserId(
            @Parameter(description = "Идентификатор пользователя")
            @PathVariable Long userId,

            @Parameter(description = "Дата голоса. Если не передана — используется сегодняшняя дата")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return mapper.toResponse(service.getByUserId(userId, Objects.requireNonNullElseGet(date, LocalDate::now)));
    }
}

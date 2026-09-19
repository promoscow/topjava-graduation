package ru.xpendence.topjavagraduation.controller.rest.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.xpendence.topjavagraduation.config.security.model.JwtUser;
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
            @RequestBody VoteRequest request,

            @AuthenticationPrincipal JwtUser jwtUser
    ) {
        return mapper.toResponse(service.create(mapper.toVote(request, jwtUser.getId())));
    }

    @GetMapping
    @Operation(summary = "Получение голоса текущего пользователя за указанную дату")
    public VoteResponse getByUserId(
            @Parameter(description = "Дата голоса. Если не передана — используется сегодняшняя дата")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,

            @AuthenticationPrincipal JwtUser jwtUser
    ) {
        return mapper.toResponse(service.getByUserId(jwtUser.getId(), Objects.requireNonNullElseGet(date, LocalDate::now)));
    }
}

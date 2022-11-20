package ru.xpendence.topjavagraduation.controller;

import ru.xpendence.topjavagraduation.controller.model.request.VoteRequest;
import ru.xpendence.topjavagraduation.controller.model.response.VoteSimpleResponse;

public interface VoteController {

    VoteSimpleResponse vote(VoteRequest request);
}

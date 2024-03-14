package com.vamae.poker.controller;

import com.vamae.poker.model.responses.GameResponse;
import com.vamae.poker.model.PokerGameSession;
import com.vamae.poker.service.mappers.GameMapper;
import com.vamae.poker.service.PokerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vamae.models.records.Settings;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/poker")
@RequiredArgsConstructor
public class PokerController {

    private final GameMapper gameMapper;
    private final PokerService pokerService;

    @GetMapping("/games")
    public List<GameResponse> getAllGames() {
        return gameMapper.toResponse(pokerService.findAll());
    }

    @PostMapping("/games")
    public PokerGameSession create(
            @RequestBody Settings settings,
            Principal principal
    ) {
        return pokerService.create(settings, principal);
    }

    @GetMapping("/join")
    public PokerGameSession join(
            @RequestBody String id,
            Principal principal
    ) {
        return pokerService.join(id, principal);
    }
}

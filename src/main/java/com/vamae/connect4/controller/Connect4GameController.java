package com.vamae.connect4.controller;

import com.vamae.connect4.service.Connect4GameService;
import com.vamae.connect4.entity.Connect4Game;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/connect4")
public class Connect4GameController {

    private final Connect4GameService connect4GameService;

    @GetMapping("/createGame")
    public Connect4Game initGame(
            @RequestParam(required = false, defaultValue = "7") int columns,
            @RequestParam(required = false, defaultValue = "6") int rows,
            @RequestParam String firstPlayerId,
            @RequestParam String secondPlayerId,
            @RequestParam int bet
    ) {
        return connect4GameService.init(columns, rows, firstPlayerId, secondPlayerId, bet);
    }

    @PostMapping("/move")
    public Connect4Game move(
            @RequestParam String gameId,
            @RequestParam int x
            ) {
        return connect4GameService.move(gameId, x);
    }
}

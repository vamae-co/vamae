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

    @GetMapping
    public Connect4Game initGame(
            @RequestParam(required = false, defaultValue = "7") int columns,
            @RequestParam(required = false, defaultValue = "6") int rows,
            @RequestParam Long firstPlayerId,
            @RequestParam Long secondPlayerId,
            @RequestParam int betSum
    ) {
        return connect4GameService.init(columns, rows, firstPlayerId, secondPlayerId, betSum);
    }
}

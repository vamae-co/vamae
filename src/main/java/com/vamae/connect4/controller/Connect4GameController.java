package com.vamae.connect4.controller;

import com.vamae.connect4.payload.request.InitializationRequest;
import com.vamae.connect4.payload.request.JoinRequest;
import com.vamae.connect4.payload.request.MoveRequest;
import com.vamae.connect4.service.Connect4GameService;
import com.vamae.connect4.entity.Connect4Game;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/connect4")
public class Connect4GameController {

    private final Connect4GameService connect4GameService;

    @GetMapping("/games")
    public List<Connect4Game> getAllGames() {
        return connect4GameService.getAllGames();
    }

    @GetMapping("/game/create")
    public Connect4Game initGame(
            @RequestBody InitializationRequest initializationRequest
            ) {
        return connect4GameService.init(initializationRequest);
    }

    @MessageMapping("/game.move")
    public Connect4Game move(
            @Payload MoveRequest moveRequest
            ) {
        return connect4GameService.move(moveRequest);
    }

    @MessageMapping("/game.join")
    public Connect4Game move(
            @Payload JoinRequest joinRequest
    ) {
        return connect4GameService.join(joinRequest);
    }
}

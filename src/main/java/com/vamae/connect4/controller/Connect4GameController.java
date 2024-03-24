package com.vamae.connect4.controller;

import com.vamae.connect4.model.dto.GameDto;
import com.vamae.connect4.payload.request.InitializationRequest;
import com.vamae.connect4.payload.request.JoinRequest;
import com.vamae.connect4.payload.request.MoveRequest;
import com.vamae.connect4.service.Connect4GameService;
import com.vamae.connect4.entity.Connect4Game;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class Connect4GameController {

    private final Connect4GameService connect4GameService;

    @GetMapping("/connect4/games")
    public List<GameDto> getAllGames() {
        return connect4GameService.getAllGames();
    }

    @PostMapping("/connect4/game/create")
    public Connect4Game initGame(
            @RequestBody InitializationRequest initializationRequest,
            Principal principal
            ) {
        return connect4GameService.init(initializationRequest, principal);
    }

    @MessageMapping("/connect4/game.move")
    @SendTo("/topic/connect4")
    public Connect4Game move(
            @Payload MoveRequest moveRequest
            ) {
        return connect4GameService.move(moveRequest);
    }

    @MessageMapping("/connect4/game.join")
    @SendTo("/topic/connect4")
    public Connect4Game join(
            @Payload JoinRequest joinRequest,
            Principal principal
    ) {
        return connect4GameService.join(joinRequest, principal);
    }
}

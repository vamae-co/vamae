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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class Connect4GameController {

    private final Connect4GameService connect4GameService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/connect4/games")
    public List<GameDto> getAllGames() {
        return connect4GameService.getAllGames();
    }

    @GetMapping("/connect4/game/{id}")
    public Connect4Game getGameById(@PathVariable String id) {
        return connect4GameService.getGameById(id);
    }

    @PostMapping("/connect4/game/create")
    public String initGame(
            @RequestBody InitializationRequest initializationRequest,
            Principal principal
            ) {
        return connect4GameService.init(initializationRequest, principal);
    }

    @MessageMapping("/connect4.move")
    public void move(
            @Payload MoveRequest moveRequest
            ) {
        simpMessagingTemplate.convertAndSendToUser(
                moveRequest.gameId(),
                "/queue/private",
                connect4GameService.move(moveRequest)
        );
    }

    @MessageMapping("/connect4.join")
    public void join(
            @Payload JoinRequest joinRequest,
            Principal principal
    ) {
        simpMessagingTemplate.convertAndSendToUser(
                joinRequest.gameId(),
                "/queue/private",
                connect4GameService.join(joinRequest, principal)
        );
    }
}

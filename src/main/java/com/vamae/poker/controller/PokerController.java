package com.vamae.poker.controller;

import com.vamae.poker.lib.models.records.Settings;
import com.vamae.poker.model.requests.GameRequest;
import com.vamae.poker.model.requests.MoveRequest;
import com.vamae.poker.model.responses.LobbyResponse;
import com.vamae.poker.model.PokerGameSession;
import com.vamae.poker.model.responses.PublicResponse;
import com.vamae.poker.service.mappers.GameMapper;
import com.vamae.poker.service.PokerService;
import com.vamae.poker.service.mappers.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/poker")
@RequiredArgsConstructor
public class PokerController {

    private final GameMapper gameMapper;
    private final ResponseMapper responseMapper;
    private final PokerService pokerService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/games")
    public List<LobbyResponse> getAllGames() {
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
            @RequestBody GameRequest request,
            Principal principal
    ) {
        return pokerService.join(request.id(), principal);
    }

    @GetMapping("/start")
    public PokerGameSession start(
            @RequestBody GameRequest request,
            Principal principal
    ) {
        return pokerService.start(request.id(), principal);
    }

    @MessageMapping("/game.move")
    @SendTo("/topic/poker")
    public PublicResponse move(
            @Payload MoveRequest request
    ) {
        PokerGameSession session = pokerService.move(request);

        session.getTable().players().forEach(
                player -> messagingTemplate.convertAndSendToUser(
                        session.getPlayersLinks().get(player.id()),
                        "/queue/private",
                        responseMapper.toPrivate(player)
                )
        );

        return responseMapper.toPublic(session);
    }
}

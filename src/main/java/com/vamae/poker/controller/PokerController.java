package com.vamae.poker.controller;

import com.vamae.poker.lib.models.records.Settings;
import com.vamae.poker.model.requests.GameRequest;
import com.vamae.poker.model.requests.MoveRequest;
import com.vamae.poker.model.responses.CreateResponse;
import com.vamae.poker.model.responses.JoinResponse;
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
import java.util.Objects;

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
    public CreateResponse create(
            @RequestBody Settings settings,
            Principal principal
    ) {
        return pokerService.create(settings, principal);
    }

    @MessageMapping("/game.join")
    @SendTo("/topic/poker")
    public JoinResponse join(
            @Payload GameRequest request,
            Principal principal
    ) {
        PokerGameSession session = pokerService.join(request.gameId(), principal);

        String newPlayerId = session.getPlayersLinks().entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), principal.getName()))
                .findAny()
                .orElseThrow()
                .getKey();

        return new JoinResponse(newPlayerId);
    }

    @MessageMapping("/game.start")
    @SendTo("/topic/poker")
    public PublicResponse start(
            @Payload GameRequest request
    ) {
        PokerGameSession session = pokerService.start(request.gameId());

        return sendResponses(session);
    }

    private PublicResponse sendResponses(PokerGameSession session) {
        session.getTable().players().forEach(
                player -> messagingTemplate.convertAndSendToUser(
                        session.getPlayersLinks().get(player.id()),
                        "/queue/private",
                        responseMapper.toPrivate(player)
                )
        );

        return responseMapper.toPublic(session);
    }

    @MessageMapping("/game.move")
    @SendTo("/topic/poker")
    public PublicResponse move(
            @Payload MoveRequest request
    ) {
        PokerGameSession session = pokerService.move(request);

        return sendResponses(session);
    }

    @MessageMapping("/game.end")
    @SendTo("/topic/poker")
    public PublicResponse end(
            @Payload GameRequest request
    ) {
        PokerGameSession session = pokerService.end(request.gameId());

        return sendResponses(session);
    }
}

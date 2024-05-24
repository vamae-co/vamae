package com.vamae.poker.controller;

import com.vamae.poker.lib.models.records.Settings;
import com.vamae.poker.model.PokerGameSession;
import com.vamae.poker.model.requests.GameRequest;
import com.vamae.poker.model.requests.MoveRequest;
import com.vamae.poker.model.responses.CreateResponse;
import com.vamae.poker.model.responses.JoinResponse;
import com.vamae.poker.model.responses.LobbyResponse;
import com.vamae.poker.service.PokerService;
import com.vamae.poker.service.mappers.LobbyMapper;
import com.vamae.poker.service.mappers.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class PokerController {

    private final LobbyMapper lobbyMapper;
    private final ResponseMapper responseMapper;
    private final PokerService pokerService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/poker/games")
    public List<LobbyResponse> getAllNotStartedGames() {
        return lobbyMapper.toResponse(pokerService.findAllNotStarted());
    }

    @PostMapping("/poker/games")
    public CreateResponse create(
            @RequestBody Settings settings,
            Principal principal
    ) {
        return pokerService.create(settings, principal);
    }

    @MessageMapping("/game.join")
    public void join(
            @Payload GameRequest request,
            Principal principal
    ) {
        PokerGameSession session = pokerService.join(request.gameId(), principal);

        String newPlayerId = session.getPlayersLinks().entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), principal.getName()))
                .findAny()
                .orElseThrow()
                .getKey();

        messagingTemplate.convertAndSendToUser(
                session.getId(),
                "/queue/private",
                new JoinResponse(newPlayerId)
        );
    }

    @MessageMapping("/game.start")
    public void start(
            @Payload GameRequest request
    ) {
        PokerGameSession session = pokerService.start(request.gameId());

        sendResponses(session);
    }

    private void sendResponses(PokerGameSession session) {
        session.getTable().players().forEach(
                player -> messagingTemplate.convertAndSendToUser(
                        session.getPlayersLinks().get(player.id()),
                        "/queue/private",
                        responseMapper.toPrivate(player)
                )
        );

        messagingTemplate.convertAndSendToUser(
                session.getId(),
                "/queue/private",
                responseMapper.toPublic(session)
        );
    }

    @MessageMapping("/game.move")
    public void move(
            @Payload MoveRequest request
    ) {
        PokerGameSession session = pokerService.move(request);

        sendResponses(session);
    }

    @MessageMapping("/game.end")
    public void end(
            @Payload GameRequest request
    ) {
        PokerGameSession session = pokerService.end(request.gameId());

        sendResponses(session);
    }
}

package com.vamae.poker.service;

import com.vamae.poker.lib.controllers.Poker;
import com.vamae.poker.lib.models.dto.TableDto;
import com.vamae.poker.lib.models.records.Settings;
import com.vamae.poker.model.PokerGameSession;
import com.vamae.poker.model.requests.MoveRequest;
import com.vamae.poker.repository.PokerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PokerService {

    private final PokerRepository repository;

    public List<PokerGameSession> findAll() {
        return repository.findAll();
    }

    public PokerGameSession create(Settings settings, Principal principal) {
        TableDto table = Poker.create(settings);

        Map<String, String> playersLinks = new HashMap<>();
        playersLinks.put(
                table.players().getLast().id(),
                principal.getName()
        );

        return repository.save(
                PokerGameSession.builder()
                        .table(table)
                        .playersLinks(playersLinks)
                        .build()
        );
    }

    public PokerGameSession join(String id, Principal principal) {
        PokerGameSession session = repository.findById(id)
                .orElseThrow();

        TableDto table = Poker.join(session.getTable());

        session.setTable(table);

        session.getPlayersLinks().put(
                table.players().getLast().id(),
                principal.getName()
        );

        return repository.save(session);
    }


    public PokerGameSession move(MoveRequest request) {
        PokerGameSession session = repository.findById(request.id())
                .orElseThrow();

        TableDto table = makeMove(request, session);

        session.setTable(table);

        return repository.save(session);
    }

    public PokerGameSession start(String id, Principal principal) {
        PokerGameSession session = repository.findById(id)
                .orElseThrow();

        TableDto tableDto = Poker.start(session.getTable());

        session.setTable(tableDto);

        return repository.save(session);
    }

    private TableDto makeMove(MoveRequest request, PokerGameSession session) {
        return switch (request.action()) {
            case BET -> Poker.bet(session.getTable(), request.amount());
            case CALL -> Poker.call(session.getTable());
            case FOLD -> Poker.fold(session.getTable());
            case CHECK -> Poker.check(session.getTable());
            case RAISE -> Poker.raise(session.getTable(), request.amount());
        };
    }
}

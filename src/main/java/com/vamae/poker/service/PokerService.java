package com.vamae.poker.service;

import com.vamae.poker.model.requests.MoveRequest;
import com.vamae.poker.model.PokerGameSession;
import com.vamae.poker.repository.PokerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vamae.controllers.Poker;
import org.vamae.models.dto.TableDto;
import org.vamae.models.records.Settings;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        session.getPlayersLinks().put(
                table.players().getLast().id(),
                principal.getName()
        );

        return repository.save(session);
    }
}

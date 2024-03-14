package com.vamae.poker.service.mappers;

import com.vamae.poker.model.responses.GameResponse;
import com.vamae.poker.model.PokerGameSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameMapper {
    public GameResponse toResponse(PokerGameSession session) {
        return GameResponse.builder()
                .id(session.getId())
                .countOfPlayers(session.getTable().players().size())
                .build();
    }

    public List<GameResponse> toResponse(List<PokerGameSession> sessions) {
        return sessions
                .stream()
                .map(this::toResponse)
                .toList();
    }
}

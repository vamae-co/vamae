package com.vamae.poker.service.mappers;

import com.vamae.poker.model.responses.LobbyResponse;
import com.vamae.poker.model.PokerGameSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameMapper {
    public LobbyResponse toResponse(PokerGameSession session) {
        return LobbyResponse.builder()
                .id(session.getId())
                .countOfPlayers(session.getTable().players().size())
                .build();
    }

    public List<LobbyResponse> toResponse(List<PokerGameSession> sessions) {
        return sessions
                .stream()
                .map(this::toResponse)
                .toList();
    }
}

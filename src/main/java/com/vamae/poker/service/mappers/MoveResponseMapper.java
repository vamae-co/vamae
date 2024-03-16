package com.vamae.poker.service.mappers;

import com.vamae.poker.lib.models.dto.PlayerDto;
import com.vamae.poker.lib.models.dto.TableDto;
import com.vamae.poker.model.PokerGameSession;
import com.vamae.poker.model.responses.PrivateResponse;
import com.vamae.poker.model.responses.PublicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoveResponseMapper {
    private final PlayerMapper playerMapper;

    public PublicResponse toPublic(PokerGameSession session) {
        TableDto table = session.getTable();
        return PublicResponse.builder()
                .id(session.getId())
                .players(playerMapper.toResponse(table.players()))
                .tableCards(table.cards())
                .pot(table.pot())
                .currentPlayerIndex(table.currentPlayerIndex())
                .currentBet(table.currentBet())
                .build();
    }

    public PrivateResponse toPrivate(PlayerDto player) {
        return PrivateResponse.builder()
                .id(player.id())
                .cards(player.hand())
                .pokerHand(player.pokerHand())
                .build();
    }
}

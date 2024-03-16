package com.vamae.poker.lib.models.dto;

import com.vamae.poker.lib.models.Deck;
import com.vamae.poker.lib.models.records.Card;
import com.vamae.poker.lib.models.records.Settings;
import lombok.Builder;

import java.util.List;

@Builder
public record TableDto(
        Settings settings,
        String state,
        List<PlayerDto> players,
        List<Card> cards,
        Deck deck,
        int pot,
        int currentPlayerIndex,
        String lastPlayerId,
        int currentBet
) {
}

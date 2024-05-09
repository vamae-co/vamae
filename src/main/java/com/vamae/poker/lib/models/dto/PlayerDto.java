package com.vamae.poker.lib.models.dto;

import com.vamae.poker.lib.models.records.Card;
import lombok.Builder;

import java.util.List;

@Builder
public record PlayerDto(
        String id,
        List<Card> hand,
        PokerHandDto pokerHand,
        int chips,
        int currentBet
) {
}

package com.vamae.poker.model.responses;

import com.vamae.poker.lib.models.records.Card;
import lombok.Builder;

import java.util.List;

@Builder
public record PublicResponse(
    String id,
    List<PlayerResponse> players,
    List<Card> tableCards,
    int pot,
    int currentPlayerIndex,
    int currentBet
) {
}

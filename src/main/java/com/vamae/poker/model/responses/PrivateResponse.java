package com.vamae.poker.model.responses;

import com.vamae.poker.lib.models.dto.PokerHandDto;
import com.vamae.poker.lib.models.records.Card;
import lombok.Builder;

import java.util.List;

@Builder
public record PrivateResponse(
        String id,
        List<Card> cards,
        PokerHandDto pokerHand
) {
}

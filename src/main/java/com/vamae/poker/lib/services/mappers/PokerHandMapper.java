package com.vamae.poker.lib.services.mappers;

import com.vamae.poker.lib.models.PokerHand;
import com.vamae.poker.lib.models.dto.PokerHandDto;

public class PokerHandMapper {
    public static PokerHandDto toDto(PokerHand pokerHand) {
        if (pokerHand == null) {
            return null;
        }

        return new PokerHandDto(
                pokerHand.getCombination(),
                CardMapper.toDto(pokerHand.getCards())
        );
    }

    public static PokerHand toPokerHand(PokerHandDto pokerHand) {
        if (pokerHand == null) {
            return null;
        }

        return new PokerHand(
                pokerHand.combination(),
                CardMapper.toCard(pokerHand.cards())
        );
    }
}

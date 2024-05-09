package com.vamae.poker.lib.services.mappers;

import com.vamae.poker.lib.models.dto.CardDto;
import com.vamae.poker.lib.models.enums.Rank;
import com.vamae.poker.lib.models.enums.Suit;
import com.vamae.poker.lib.models.records.Card;

import java.util.List;
import java.util.stream.Collectors;

public class CardMapper {
    public static CardDto toDto(Card card) {
        return new CardDto(card.suit().toString(), card.rank().toString());
    }

    public static Card toCard(CardDto card) {
        return new Card(Suit.valueOf(card.suit()), Rank.valueOf(card.rank()));
    }

    public static List<CardDto> toDto(List<Card> cards) {
        return cards.stream()
                .map(CardMapper::toDto)
                .toList();
    }

    public static List<Card> toCard(List<CardDto> cards) {
        return cards.stream()
                .map(CardMapper::toCard)
                .collect(Collectors.toList());
    }
}

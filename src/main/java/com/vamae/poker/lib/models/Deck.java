package com.vamae.poker.lib.models;

import com.vamae.poker.lib.models.enums.Rank;
import com.vamae.poker.lib.models.enums.Suit;
import com.vamae.poker.lib.models.records.Card;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Setter
public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();

        Arrays.stream(Suit.values())
                .forEach(suit -> Arrays.stream(Rank.values())
                        .forEach(rank -> cards.add(new Card(suit, rank))));

        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card deal() {
        return cards.removeFirst();
    }
}

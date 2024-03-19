package com.vamae.poker.lib.models;

import com.vamae.poker.lib.models.enums.Combination;
import com.vamae.poker.lib.models.records.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PokerHand {
    private Combination combination;
    private List<Card> cards;
}

package com.vamae.poker.lib.models;

import com.vamae.poker.lib.models.records.Card;
import com.vamae.poker.lib.services.PokerRules;
import com.vamae.poker.lib.services.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Player {
    private final String id;
    private final Table table;
    private List<Card> hand;
    private PokerHand pokerHand;
    private int chips;
    private int currentBet;
    @Setter
    private boolean isFolded;
    private boolean isAllIn;

    public Player(Table table){
        this.id = UUID.randomUUID().toString();
        this.table = table;
        chips = 1000;
        dropOldInfo();
    }

    public void dropOldInfo() {
        hand = new ArrayList<>();
        currentBet = 0;
        isAllIn = false;
        isFolded = false;
    }

    public void take(Card card) {
        hand.add(card);
        updatePokerHand();
    }

    public void updatePokerHand() {
        pokerHand = PokerRules.check(table.getCards(), hand);
    }

    public int bet(int amount) {
        currentBet += amount;
        return subtractChipsOrAllIn(amount);
    }

    protected int subtractChipsOrAllIn(int amount) {
        if (amount > chips) {
            int result = chips;
            chips = 0;
            isAllIn = true;
            return result;
        }
        chips -= amount;
        return amount;
    }

    public void addChips(int amount) {
        chips += amount;
    }
}

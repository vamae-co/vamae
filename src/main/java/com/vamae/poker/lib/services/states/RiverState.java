package com.vamae.poker.lib.services.states;

import com.vamae.poker.lib.models.Player;
import com.vamae.poker.lib.services.Table;

public class RiverState extends GameState {
    public RiverState(Table table) {
        super(table);
    }

    @Override
    public void init() {
        table.dealCard(deck.deal());
    }

    @Override
    public void join() {
    }

    @Override
    public void start() {
    }

    @Override
    public void end() {

    }

    @Override
    protected void changeStateIfNeedsAndMoveToNextPlayer(Player player) {
        if (player.getId().equals(lastPlayerId)) {
            table.changeState(new ShowdownState(table));
        } else {
            table.moveToNextPlayer();
        }
    }

    @Override
    public String toString() {
        return "River";
    }
}

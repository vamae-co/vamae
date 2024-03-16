package com.vamae.poker.lib.services.states;

import com.vamae.poker.lib.models.Player;
import com.vamae.poker.lib.services.Table;

public class WaitingState extends GameState {
    public WaitingState(Table table) {
        super(table);
    }

    @Override
    public void init() {
    }

    @Override
    public void join() {
        Player player = new Player(table);
        players.add(player);
    }

    @Override
    public void start() {
        if (players.size() > 1) {
            table.changeState(new PreFlopState(table));
        }
    }

    @Override
    public void end() {

    }

    @Override
    protected void changeStateIfNeedsAndMoveToNextPlayer(Player player) {
    }

    @Override
    public void onCheck() {
    }

    @Override
    public void onCall() {
    }

    @Override
    public void onBet(int amount) {
    }

    @Override
    public void onFold() {
    }

    @Override
    public void onRaise(int callAndRaise) {
    }

    @Override
    public String toString() {
        return "Waiting players";
    }
}

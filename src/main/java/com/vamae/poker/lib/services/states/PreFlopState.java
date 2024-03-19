package com.vamae.poker.lib.services.states;

import com.vamae.poker.lib.models.Deck;
import com.vamae.poker.lib.models.Player;
import com.vamae.poker.lib.services.Table;

import java.util.ArrayList;

public class PreFlopState extends GameState {
    public PreFlopState(Table table) {
        super(table);
    }

    @Override
    public void init() {
        int smallBlind = table.getSettings().smallBlind();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);

            if (player.getChips() < smallBlind * 2) {
                players.remove(player);
                continue;
            }

            player.dropOldInfo();
        }
        if (players.size() < 2) {
            players.forEach(Player::dropOldInfo);

            table.changeState(new WaitingState(table));
        } else {
            table.dropCurrentBet();

            table.setDeck(new Deck());
            deck = table.getDeck();

            table.setCards(new ArrayList<>());
            cards = table.getCards();

            blind(smallBlind);
            blind(smallBlind * 2);

            shiftPlayers(2);

            updateLastPlayer();

            players.forEach(player -> {
                player.setFolded(false);
                player.take(deck.deal());
                player.take(deck.deal());
            });
        }
    }

    private void blind(int amount) {
        Player player = table.getCurrentPlayer();
        player.bet(amount);
        table.addToPot(amount);
        table.moveToNextPlayer();
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
            table.changeState(new FlopState(table));
        } else {
            table.moveToNextPlayer();
        }
    }

    @Override
    public String toString() {
        return "PreFlop";
    }
}

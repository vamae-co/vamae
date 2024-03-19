package com.vamae.poker.lib.services.states;

import com.vamae.poker.lib.models.Deck;
import com.vamae.poker.lib.models.Player;
import com.vamae.poker.lib.models.records.Card;
import com.vamae.poker.lib.services.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public abstract class GameState {
    protected final Table table;
    protected List<Player> players;
    protected List<Card> cards;
    protected Deck deck;
    @Setter
    @Getter
    protected String lastPlayerId;

    public GameState(Table table) {
        table.setCurrentPlayerIndex(0);
        this.table = table;
        players = table.getPlayers();
        deck = table.getDeck();
        cards = table.getCards();
        updateLastPlayer();
    }

    public abstract void init();

    public abstract void join();

    public abstract void start();

    public abstract void end();

    protected abstract void changeStateIfNeedsAndMoveToNextPlayer(Player player);

    public void onCheck() {
        Player player = table.getCurrentPlayer();
        if (player.getCurrentBet() == table.getCurrentBet()) {
            changeStateIfNeedsAndMoveToNextPlayer(player);
        }
    }

    public void onCall() {
        Player player = table.getCurrentPlayer();
        int amountToCall = table.getCurrentBet() - player.getCurrentBet();
        int chips = player.bet(amountToCall);

        if (player.isAllIn()) {
            updateLastPlayer();
        }

        table.addToPot(chips);
        changeStateIfNeedsAndMoveToNextPlayer(player);
    }

    protected void updateLastPlayer() {
        if (!players.isEmpty()) {
            lastPlayerId = players.getLast().getId();
        }
    }

    public void onBet(int amount) {
        Player player = table.getCurrentPlayer();
        betAndUpdateLastPlayer(amount, player, amount);
    }

    private void betAndUpdateLastPlayer(int bet, Player player, int raiseCurrentBet) {
        if (raiseCurrentBet >= table.getSettings().smallBlind() * 2
                && bet <= player.getChips()) {
            player.bet(bet);
            table.addToPot(bet);
            table.addToCurrentBet(raiseCurrentBet);
            shiftPlayers(table.getCurrentPlayerIndex());
            updateLastPlayer();
            table.moveToNextPlayer();
        }
    }

    protected void shiftPlayers(int offset) {
        List<Player> movedList = new ArrayList<>();
        movedList.addAll(players.subList(offset, players.size()));
        movedList.addAll(players.subList(0, offset));
        players = movedList;
    }

    public void onFold() {
        Player player = table.getCurrentPlayer();
        player.setFolded(true);

        updateLastPlayer();

        if (table.countUnfoldedPlayers() == 1) {
            table.changeState(new ShowdownState(table));
        } else {
            changeStateIfNeedsAndMoveToNextPlayer(player);
        }
    }

    public void onRaise(int callAndRaise) {
        Player player = table.getCurrentPlayer();
        int call = table.getCurrentBet() - player.getCurrentBet();
        int raise = callAndRaise - call;
        betAndUpdateLastPlayer(callAndRaise, player, raise);
    }
}

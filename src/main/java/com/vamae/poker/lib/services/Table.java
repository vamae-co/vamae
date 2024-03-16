package com.vamae.poker.lib.services;

import com.vamae.poker.lib.models.Deck;
import com.vamae.poker.lib.models.Player;
import com.vamae.poker.lib.models.PokerHand;
import com.vamae.poker.lib.models.enums.Combination;
import com.vamae.poker.lib.models.records.Card;
import com.vamae.poker.lib.models.records.Settings;
import com.vamae.poker.lib.services.states.GameState;
import com.vamae.poker.lib.services.states.WaitingState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Table {
    private final Settings settings;
    @Setter
    private GameState state;
    @Setter
    private List<Player> players;
    @Setter
    private List<Card> cards;
    @Setter
    private Deck deck;
    private int pot;
    @Setter
    private int currentPlayerIndex;
    private int currentBet;

    public Table(Settings settings) {
        this.settings = settings;
        players = new ArrayList<>();
        pot = 0;
        state = new WaitingState(this);
        dropCurrentBet();
    }

    public void dropCurrentBet() {
        currentBet = settings.smallBlind() * 2;
    }

    public void join() {
        state.join();
    }

    public void start() {
        state.start();
    }

    public void end() {
        state.end();
    }

    public void check() {
        if (getCurrentPlayer().getCurrentBet() == currentBet) {
            state.onCheck();
        }
    }

    public void call() {
        if (getCurrentPlayer().getCurrentBet() != currentBet) {
            state.onCall();
        }
    }

    public void bet(int amount) {
        if (getCurrentPlayer().getCurrentBet() == currentBet) {
            state.onBet(amount);
        }
    }

    public void fold() {
        if (getCurrentPlayer().getCurrentBet() != currentBet) {
            state.onFold();
        }
    }

    public void raise(int callAndRaise) {
        if (getCurrentPlayer().getCurrentBet() != currentBet) {
            state.onRaise(callAndRaise);
        }
    }

    public void findAndRewardWinner() {
        Combination strongest = findStrongestCombination();
        List<Player> winners = findPlayersWithCombination(strongest);

        if (winners.isEmpty()) return;

        if (winners.size() == 1) {
            rewardSingleWinner(winners.getFirst());
        } else {
            rewardMultipleWinnersEqually(findWinnersWithHighestScore(winners));
        }

        resetPot();
    }

    private Combination findStrongestCombination() {
        return players.stream()
                .map(player -> player.getPokerHand().getCombination())
                .max(Enum::compareTo)
                .orElse(Combination.HIGH_CARD);
    }

    private List<Player> findPlayersWithCombination(Combination combination) {
        return players.stream()
                .filter(player -> player.getPokerHand().getCombination() == combination)
                .toList();
    }

    private void rewardSingleWinner(Player winner) {
        winner.addChips(pot);
    }

    private List<Player> findWinnersWithHighestScore(List<Player> winners) {
        int highestScore = winners.stream()
                .mapToInt(player -> calculateHandScore(player.getPokerHand()))
                .max()
                .orElse(0);

        return winners.stream()
                .filter(player -> calculateHandScore(player.getPokerHand()) == highestScore)
                .toList();
    }

    private int calculateHandScore(PokerHand hand) {
        return hand.getCards().stream()
                .mapToInt(card -> card.rank().ordinal())
                .sum();
    }

    private void rewardMultipleWinnersEqually(List<Player> winners) {
        int totalReward = pot;
        int rewardPerWinner = totalReward / winners.size();
        int remainder = totalReward % winners.size();

        winners.forEach(player -> player.addChips(rewardPerWinner));

        for (int i = 0; i < remainder; i++) {
            winners.get(i).addChips(1);
        }
    }

    private void resetPot() {
        pot = 0;
    }

    public void changeState(GameState newState) {
        state = newState;
        state.init();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void moveToNextPlayer() {
        do {
            currentPlayerIndex++;
            if (currentPlayerIndex >= players.size()) {
                currentPlayerIndex = 0;
            }
        } while (getCurrentPlayer().isFolded());
    }

    public int countUnfoldedPlayers() {
        return (int) players
                .stream()
                .filter(player -> !player.isFolded())
                .count();
    }

    public void addToPot(int amount) {
        pot += amount;
    }

    public void addToCurrentBet(int amount) {
        currentBet += amount;
    }

    public void dealCard(Card card) {
        cards.add(card);
        players.forEach(Player::updatePokerHand);
    }

    public void dealCard(List<Card> cards) {
        cards.forEach(this::dealCard);
    }
}

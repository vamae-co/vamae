package com.vamae.poker.lib.services;

import com.vamae.poker.lib.models.PokerHand;
import com.vamae.poker.lib.models.enums.Combination;
import com.vamae.poker.lib.models.enums.Rank;
import com.vamae.poker.lib.models.enums.Suit;
import com.vamae.poker.lib.models.records.Card;

import java.util.*;
import java.util.stream.Collectors;

public class PokerRules {
    public static PokerHand check(List<Card> tableCards, List<Card> playerCards) {
        List<Card> allCards = combineCards(tableCards, playerCards);
        allCards.sort(Comparator.comparing(Card::rank));

        Map<Rank, Long> rankFrequency = getRankFrequency(allCards);

        return determineBestHand(rankFrequency, allCards);
    }

    private static List<Card> combineCards(List<Card> tableCards, List<Card> playerCards) {
        List<Card> allCards = new ArrayList<>(tableCards);
        allCards.addAll(playerCards);
        return allCards;
    }

    private static Map<Rank, Long> getRankFrequency(List<Card> allCards) {
        return allCards
                .stream()
                .collect(Collectors.groupingBy(Card::rank, Collectors.counting()));
    }

    private static PokerHand determineBestHand(Map<Rank, Long> rankFrequency, List<Card> allCards) {
        List<Card> bestComboCards = new ArrayList<>(List.of(allCards.getLast()));
        Combination bestHand = Combination.HIGH_CARD;

        if (rankFrequency.containsValue(4L)) {
            bestHand = Combination.FOUR_OF_A_KIND;
            bestComboCards = determineFourOfAKind(rankFrequency, allCards);
        } else if (hasFullHouse(rankFrequency)) {
            bestHand = Combination.FULL_HOUSE;
            bestComboCards = determineFullHouse(rankFrequency, allCards);
        } else if (rankFrequency.containsValue(3L)) {
            bestHand = Combination.THREE_OF_A_KIND;
            bestComboCards = determineThreeOfAKind(rankFrequency, allCards);
        } else if (hasTwoPair(rankFrequency)) {
            bestHand = Combination.TWO_PAIR;
            bestComboCards = determineTwoPair(rankFrequency, allCards);
        } else if (
                rankFrequency.values()
                        .stream()
                        .anyMatch(count -> count == 2)
        ) {
            bestHand = Combination.ONE_PAIR;
            bestComboCards = determineOnePair(rankFrequency, allCards);
        }

        Optional<List<Card>> straight = getHighestStraight(allCards);
        if (straight.isPresent()) {
            if (getFlushCards(straight.get()).isPresent()) {
                bestHand = Combination.STRAIGHT_FLUSH;
            } else {
                bestHand = Combination.STRAIGHT;
            }
            bestComboCards = straight.get();
        } else if (getFlushCards(allCards).isPresent()) {
            bestHand = Combination.FLUSH;
            bestComboCards = getFlushCards(allCards).get();
        }

        return new PokerHand(bestHand, bestComboCards);
    }

    private static List<Card> determineFourOfAKind(Map<Rank, Long> rankFrequency, List<Card> allCards) {
        Rank rank = rankFrequency.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == 4)
                .map(Map.Entry::getKey)
                .findAny()
                .orElse(null);
        return allCards
                .stream()
                .filter(card -> card.rank() == rank)
                .toList();
    }

    private static boolean hasFullHouse(Map<Rank, Long> rankFrequency) {
        return rankFrequency.containsValue(3L) && rankFrequency.containsValue(2L)
                || rankFrequency.values()
                .stream()
                .filter(count -> count == 3)
                .count() > 1;
    }

    private static List<Card> determineFullHouse(Map<Rank, Long> rankFrequency, List<Card> allCards) {
        List<Rank> ranks = rankFrequency.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == 3 || entry.getValue() == 2)
                .sorted(
                        Comparator
                                .comparing(entry -> ((Map.Entry<Rank, Long>) entry).getValue())
                                .thenComparing(entry -> ((Map.Entry<Rank, Long>) entry).getKey())
                )
                .map(Map.Entry::getKey)
                .toList();
        List<Card> bestComboCards = allCards
                .stream()
                .filter(card -> card.rank() == ranks.getLast())
                .collect(Collectors.toList());
        bestComboCards.addAll(allCards
                .stream()
                .filter(card -> card.rank() == ranks.get(ranks.size() - 2))
                .limit(2)
                .toList());
        return bestComboCards;
    }

    private static List<Card> determineThreeOfAKind(Map<Rank, Long> rankFrequency, List<Card> allCards) {
        Rank rank = rankFrequency.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == 3)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        return allCards
                .stream()
                .filter(card -> card.rank() == rank)
                .collect(Collectors.toList());
    }

    private static boolean hasTwoPair(Map<Rank, Long> rankFrequency) {
        return rankFrequency.values()
                .stream()
                .filter(count -> count == 2)
                .count() > 1;
    }

    private static List<Card> determineTwoPair(Map<Rank, Long> rankFrequency, List<Card> allCards) {
        List<Rank> ranks = rankFrequency.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == 2)
                .map(Map.Entry::getKey)
                .sorted()
                .toList();
        List<Card> bestComboCards = allCards
                .stream()
                .filter(card -> card.rank() == ranks.getLast())
                .collect(Collectors.toList());
        bestComboCards.addAll(
                allCards
                        .stream()
                        .filter(card -> card.rank() == ranks.get(ranks.size() - 2))
                        .toList()
        );
        return bestComboCards;
    }

    private static List<Card> determineOnePair(Map<Rank, Long> rankFrequency, List<Card> allCards) {
        Rank rank = rankFrequency.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == 2)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        return allCards
                .stream()
                .filter(card -> card.rank() == rank)
                .collect(Collectors.toList());
    }

    private static List<Card> getAllCardsWithRankFromList(List<Card> allCards, Rank rank) {
        return allCards
                .stream()
                .filter(card -> card.rank() == rank)
                .toList();
    }

    private static Optional<List<Card>> getHighestStraight(List<Card> cards) {
        List<Card> straight = new ArrayList<>();

        for (int i = cards.size() - 5; i >= 0; i--) {
            boolean isStraight = true;
            for (int j = i + 1; j < i + 5; j++) {
                if (cards.get(j).rank().ordinal() != cards.get(j - 1).rank().ordinal() + 1) {
                    isStraight = false;
                    break;
                }
            }
            if (isStraight) {
                straight.addAll(cards.subList(i, i + 5));
                break;
            }
        }
        return straight.isEmpty() ? Optional.empty() : Optional.of(straight);
    }

    private static Optional<List<Card>> getFlushCards(List<Card> cards) {
        Map<Suit, List<Card>> suitMap = cards
                .stream()
                .collect(Collectors.groupingBy(Card::suit));
        return suitMap.values()
                .stream()
                .filter(list -> list.size() >= 5)
                .map(
                        list -> list
                                .stream()
                                .sorted(Comparator.comparing(Card::rank).reversed())
                                .limit(5)
                                .toList()
                )
                .findAny();
    }


}
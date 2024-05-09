package com.vamae.poker.lib.models.records;


import com.vamae.poker.lib.models.enums.Rank;
import com.vamae.poker.lib.models.enums.Suit;

public record Card(Suit suit, Rank rank) {
}

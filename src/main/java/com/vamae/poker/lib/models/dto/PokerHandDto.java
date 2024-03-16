package com.vamae.poker.lib.models.dto;


import com.vamae.poker.lib.models.enums.Combination;

import java.util.List;

public record PokerHandDto(Combination combination, List<CardDto> cards) {
}

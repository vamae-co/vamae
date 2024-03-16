package com.vamae.poker.model.responses;

import lombok.Builder;

@Builder
public record PlayerResponse(String id, int chips, int currentBet) {
}

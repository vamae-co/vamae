package com.vamae.poker.model.responses;

import lombok.Builder;

@Builder
public record GameResponse(String id, int countOfPlayers) {
}

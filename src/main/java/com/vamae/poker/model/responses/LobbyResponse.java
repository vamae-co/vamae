package com.vamae.poker.model.responses;

import lombok.Builder;

@Builder
public record LobbyResponse(String id, int countOfPlayers) {
}

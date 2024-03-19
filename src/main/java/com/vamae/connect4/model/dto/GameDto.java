package com.vamae.connect4.model.dto;

import lombok.Builder;

@Builder
public record GameDto (
        String gameId,
        String firstPlayerUsername,
        int bet
) {
}

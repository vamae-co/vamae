package com.vamae.connect4.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record InitializationRequest (
        @NotNull
        int columns,
        @NotNull
        int rows,
        @NotBlank
        String firstPlayerId,
        @NotNull
        int bet
) {
}

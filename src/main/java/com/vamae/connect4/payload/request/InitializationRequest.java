package com.vamae.connect4.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record InitializationRequest (
        @NotNull
        int columns,
        @NotNull
        int rows,
        @NotNull
        int bet
) {
}

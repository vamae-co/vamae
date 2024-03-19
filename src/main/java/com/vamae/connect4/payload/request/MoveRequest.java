package com.vamae.connect4.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MoveRequest (
        @NotNull
        int columnIndex,
        @NotBlank
        String gameId
) {
}

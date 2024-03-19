package com.vamae.poker.model.requests;

import jakarta.validation.constraints.NotBlank;

public record GameRequest(
        @NotBlank
        String gameId
) {
}

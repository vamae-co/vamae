package com.vamae.connect4.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record JoinRequest (
        @NotBlank
        String gameId
) {
}

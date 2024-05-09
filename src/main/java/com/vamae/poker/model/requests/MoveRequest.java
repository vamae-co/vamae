package com.vamae.poker.model.requests;

import com.vamae.poker.model.enums.Action;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record MoveRequest(
        @NotBlank
        String id,
        @NotBlank
        Action action,
        @Positive
        int amount) {
}

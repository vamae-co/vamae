package com.vamae.statistic.payload.response;

import jakarta.validation.constraints.NotBlank;

public record StatisticResponse(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Authorisation count is required")
        int authTimes
) {
}

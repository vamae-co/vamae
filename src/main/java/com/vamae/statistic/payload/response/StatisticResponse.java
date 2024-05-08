package com.vamae.statistic.payload.response;

public record StatisticResponse(
        String username,
        int authCount
) {
}

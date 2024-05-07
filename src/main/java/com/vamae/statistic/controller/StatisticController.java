package com.vamae.statistic.controller;

import com.vamae.statistic.model.Statistic;
import com.vamae.statistic.payload.response.StatisticResponse;
import com.vamae.statistic.service.StatisticService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    // Mapping to get statistics for the authenticated user
    @GetMapping("/statistic")
    public StatisticResponse getStatistic(@RequestParam String username) {
        Statistic statistic = statisticService.getStatistic(username);
        return new StatisticResponse(statistic.getUsername(), statistic.getAuthTimes());
    }

}

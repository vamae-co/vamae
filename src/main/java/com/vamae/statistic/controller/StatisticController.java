package com.vamae.statistic.controller;

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
        return statisticService.getStatistic(username);
    }

}

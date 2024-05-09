package com.vamae.statistic.service;

import com.vamae.statistic.model.Statistic;
import com.vamae.statistic.payload.response.StatisticResponse;
import com.vamae.statistic.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final StatisticRepository statisticRepository;

    public StatisticResponse getStatistic(String username) {
        Statistic statistic = statisticRepository.findByUsername(username)
                .orElseThrow(
                        () -> new IllegalArgumentException(String.format("Statistic with username %s not found", username))
                );
        return new StatisticResponse(username, statistic.getAuthCount());
    }

    @Transactional
    public StatisticResponse updateAuthCount(String username) {
        Optional<Statistic> statistic = statisticRepository.findByUsername(username);

        if (statistic.isPresent()) {
            int authTimes = statistic.get().getAuthCount();
            if (authTimes == 0) {
                statistic.get().setAuthCount(1);
                statisticRepository.save(statistic.get());
            } else {
                statistic.get().setAuthCount(authTimes + 1);
                statisticRepository.save(statistic.get());
            }
            return new StatisticResponse(username, statistic.get().getAuthCount());
        } else {
            Statistic newStatistic = statisticRepository.save(new Statistic(username, 1));
            return new StatisticResponse(username, newStatistic.getAuthCount());
        }
    }
}

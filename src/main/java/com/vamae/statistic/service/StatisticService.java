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

    @Transactional
    public StatisticResponse updateAuthCount(String username) {
        Optional<Statistic> statistic = statisticRepository.findByUsername(username);

        if (statistic.isPresent()) {
            Statistic presentStatistic = statistic.get();
            int authCount = presentStatistic.getAuthCount();
            statisticRepository.delete(presentStatistic);
            Statistic newStatistic = new Statistic(username, authCount + 1);
            statisticRepository.save(newStatistic);
            return new StatisticResponse(username, newStatistic.getAuthCount());
        }
        else {
            Statistic newStatistic = statisticRepository.save(new Statistic(username, 1));
            return new StatisticResponse(username, newStatistic.getAuthCount());
        }
    }
    public StatisticResponse getStatistic(String username) {
        Statistic statistic = statisticRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException(String.format("Statistic with username %s not found", username)));
        return new StatisticResponse(username, statistic.getAuthCount());
    }

    //    Method with updating mongodb value (doesn't work)
    //    public StatisticResponse updateAuthTimes(String username) {
    //        Optional<Statistic> statistic = statisticRepository.findByUsername(username);
    //
    //        if (statistic.isPresent()) {
    //            int authTimes = statistic.get().getAuthTimes();
    //            if(authTimes == 0) {
    //                statistic.get().setAuthTimes(1);
    //                statisticRepository.save(statistic.get());
    //            }
    //            else {
    //                statistic.get().setAuthTimes(authTimes + 1);
    //                statisticRepository.save(statistic.get());
    //            }
    //            return new StatisticResponse(username, statistic.get().getAuthTimes());
    //        }
    //        else {
    //           Statistic newStatistic = statisticRepository.save(new Statistic(username, 1));
    //           return new StatisticResponse(username, newStatistic.getAuthTimes());
    //        }
    //    }
}

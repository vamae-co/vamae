package com.vamae.statistic.service;

import com.vamae.authorization.model.User;
import com.vamae.authorization.repository.UserRepository;
import com.vamae.authorization.service.UserService;
import com.vamae.statistic.model.Statistic;
import com.vamae.statistic.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final StatisticRepository statisticRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Transactional
    public void getAuthTimes(String username) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);

        if (userOptional.isPresent()) {
            Statistic statistic = statisticRepository.findByUsername(username)
                    .orElseGet(() -> Statistic.builder().username(username).authTimes(0).build());

            statistic.setAuthTimes(statistic.getAuthTimes() + 1);

            statisticRepository.save(statistic);
        }
    }

    public Statistic getStatistic(String username) {
        return statisticRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Statistic not found"));
    }

}

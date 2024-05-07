package com.vamae.statistic.repository;

import com.vamae.statistic.model.Statistic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatisticRepository extends MongoRepository<Statistic, String> {
    Optional<Statistic> findByUsername(String username);
}

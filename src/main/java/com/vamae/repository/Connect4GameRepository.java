package com.vamae.repository;

import com.vamae.entity.Connect4Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Connect4GameRepository extends MongoRepository<Connect4Game, String> {
}
package com.vamae.poker.repository;

import com.vamae.poker.model.PokerGameSession;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PokerRepository extends MongoRepository<PokerGameSession, String> {
}

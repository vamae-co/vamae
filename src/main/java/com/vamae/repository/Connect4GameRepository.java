package com.vamae.repository;

import com.vamae.entity.Connect4Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Connect4GameRepository extends JpaRepository<Connect4Game, Long> {
}
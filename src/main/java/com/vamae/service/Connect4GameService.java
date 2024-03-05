package com.vamae.service;

import com.vamae.entity.Connect4Game;
import com.vamae.repository.Connect4GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.vamae.controller.Connect4;
import org.vamae.controller.GameBoardController;
import org.vamae.entity.GameBoard;
import org.vamae.enums.Piece;

@Service
@AllArgsConstructor
public class Connect4GameService {
    Connect4GameRepository connect4GameRepository;

    public Connect4Game init(int columns, int rows, Long firstPlayerId, Long secondPlayerId, int betSum) {
        Connect4 game = new Connect4(new GameBoardController(new GameBoard(columns, rows)));
        Connect4Game connect4Game = Connect4Game.builder()
                .firstPlayerId(firstPlayerId)
                .secondPlayerId(secondPlayerId)
                .betSum(betSum)
                .currentPlayer(Piece.PLAYER_1)
                .build();
        return connect4GameRepository.save(connect4Game);
    }
}

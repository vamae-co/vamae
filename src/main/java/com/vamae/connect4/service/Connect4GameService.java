package com.vamae.connect4.service;

import com.vamae.connect4.entity.Connect4Game;
import com.vamae.connect4.repository.Connect4GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.vamae.controller.Connect4;
import org.vamae.controller.GameBoardController;
import org.vamae.entity.GameBoard;
import org.vamae.enums.Piece;

@AllArgsConstructor
@Service
public class Connect4GameService {
    Connect4GameRepository connect4GameRepository;

    public Connect4Game init(int columns, int rows, Long firstPlayerId, Long secondPlayerId, int betSum) {
        Connect4 game = new Connect4(new GameBoardController(new GameBoard(columns, rows)));
        Connect4Game connect4Game = Connect4Game.builder()
                .firstPlayerId(firstPlayerId)
                .secondPlayerId(secondPlayerId)
                .betSum(betSum)
                .game(game)
                .currentPlayer(Piece.PLAYER_1)
                .build();
        return connect4GameRepository.save(connect4Game);
    }

    public Connect4Game move(String gameId, int x) {
        Connect4Game currentGame = connect4GameRepository.findById(gameId).get();
        Connect4 currentGameObject = currentGame.getGame();

        currentGameObject.move(x, currentGame.getCurrentPlayer());

        if(currentGame.getCurrentPlayer() == Piece.PLAYER_1) {
            currentGame.setCurrentPlayer(Piece.PLAYER_2);
        }
        else {
            currentGame.setCurrentPlayer(Piece.PLAYER_1);
        }

        currentGame.setGame(currentGameObject);
        return connect4GameRepository.save(currentGame);
    }

    public int endGame(String gameId) {
        Connect4Game currentGame = connect4GameRepository.findById(gameId).get();
        int winSum = currentGame.getBetSum();
        connect4GameRepository.delete(currentGame);
        return winSum;
    }
}

package com.vamae.service;

import com.vamae.entity.Connect4Game;
import com.vamae.repository.Connect4GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vamae.controller.Connect4;
import org.vamae.controller.GameBoardController;
import org.vamae.entity.GameBoard;
import org.vamae.enums.Piece;

@AllArgsConstructor
@Service
public class Connect4GameService {
    Connect4GameRepository connect4GameRepository;

    public Connect4Game init(int columns, int rows, Long firstPlayerId, Long secondPlayerId, Double betSum) {
        Connect4 game = new Connect4(new GameBoardController(new GameBoard(columns, rows)));
        Connect4Game connect4Game = Connect4Game.builder()
                .firstPlayerId(firstPlayerId)
                .secondPlayerId(secondPlayerId)
                .betSum(betSum)
                .game(game)
                .currentPlayer(Piece.PLAYER_1)
                .build();
        System.out.println("GAME INITIALIZATION");
        return connect4GameRepository.save(connect4Game);
    }

    public Connect4Game move(String gameId, int x) {
        Connect4Game currentGame = connect4GameRepository.findById(gameId).get();
        Connect4 currentGameObject = currentGame.getGame();

        if(currentGame.getCurrentPlayer() == Piece.PLAYER_1) {
            currentGameObject.move(x, currentGame.getCurrentPlayer());
            currentGame.setCurrentPlayer(Piece.PLAYER_2);
        }
        else {
            currentGameObject.move(x, currentGame.getCurrentPlayer());
            currentGame.setCurrentPlayer(Piece.PLAYER_1);
        }

        currentGame.setGame(currentGameObject);
        return connect4GameRepository.save(currentGame);
    }

    public Double endGame(String gameId) {
        Connect4Game currentGame = connect4GameRepository.findById(gameId).get();
        Double winSum = currentGame.getBetSum();
        connect4GameRepository.delete(currentGame);
        return winSum;
    }
}

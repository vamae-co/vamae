package com.vamae.connect4.service;

import com.vamae.authorization.service.UserService;
import com.vamae.connect4.entity.Connect4Game;
import com.vamae.connect4.repository.Connect4GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.vamae.connect4lib.controller.Connect4;
import com.vamae.connect4lib.controller.GameBoardController;
import com.vamae.connect4lib.entity.GameBoard;
import com.vamae.connect4lib.enums.Piece;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class Connect4GameService {
    private final Connect4GameRepository connect4GameRepository;
    private final UserService userService;

    public Connect4Game init(int columns, int rows, String firstPlayerId, String secondPlayerId, int bet) {
        Connect4 game = new Connect4(new GameBoardController(new GameBoard(columns, rows)));
        Connect4Game connect4Game = Connect4Game.builder()
                .firstPlayerId(firstPlayerId)
                .secondPlayerId(secondPlayerId)
                .betSum(bet * 2)
                .game(game)
                .currentPlayer(Piece.PLAYER_1)
                .isWinFlag(false)
                .build();

        userService.changeBalance(firstPlayerId, -bet);
        userService.changeBalance(secondPlayerId, -bet);

        return connect4GameRepository.save(connect4Game);
    }

    public Connect4Game move(String gameId, int x) {
        Connect4Game currentGame = connect4GameRepository.findById(gameId)
                .orElseThrow(() -> new NoSuchElementException("Game not found!"));
        Connect4 currentGameObject = currentGame.getGame();

        boolean isWin = currentGameObject.move(x, currentGame.getCurrentPlayer());
        if(isWin) {
            currentGame.setWinFlag(true);
            return endGame(currentGame);
        }

        changeCurrentPlayer(currentGame);

        currentGame.setGame(currentGameObject);
        return connect4GameRepository.save(currentGame);
    }

    private Connect4Game endGame(Connect4Game game) {
        if(game.getCurrentPlayer() == Piece.PLAYER_1) {
            userService.changeBalance(game.getFirstPlayerId(), game.getBetSum());
        }
        else {
            userService.changeBalance(game.getSecondPlayerId(), game.getBetSum());
        }

        connect4GameRepository.delete(game);
        return game;
    }

    private static void changeCurrentPlayer(Connect4Game currentGame) {
        if(currentGame.getCurrentPlayer() == Piece.PLAYER_1) {
            currentGame.setCurrentPlayer(Piece.PLAYER_2);
        }
        else {
            currentGame.setCurrentPlayer(Piece.PLAYER_1);
        }
    }
}

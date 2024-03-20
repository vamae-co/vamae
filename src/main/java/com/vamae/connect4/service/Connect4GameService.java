package com.vamae.connect4.service;

import com.vamae.authorization.service.UserService;
import com.vamae.connect4.entity.Connect4Game;
import com.vamae.connect4.mapper.GameMapper;
import com.vamae.connect4.model.dto.GameDto;
import com.vamae.connect4.payload.request.InitializationRequest;
import com.vamae.connect4.payload.request.JoinRequest;
import com.vamae.connect4.payload.request.MoveRequest;
import com.vamae.connect4.repository.Connect4GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.vamae.connect4.lib.controller.Connect4;
import com.vamae.connect4.lib.controller.GameBoardController;
import com.vamae.connect4.lib.entity.GameBoard;
import com.vamae.connect4.lib.enums.Piece;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class Connect4GameService {

    private final Connect4GameRepository connect4GameRepository;
    private final UserService userService;
    private final GameMapper gameMapper;

    public Connect4Game init(InitializationRequest initializationRequest, Principal principal) {
        Connect4 game = new Connect4(
                new GameBoardController(
                        new GameBoard(initializationRequest.columns(), initializationRequest.rows())
                )
        );
        Connect4Game connect4Game = Connect4Game.builder()
                .firstPlayerUsername(principal.getName())
                .betSum(initializationRequest.bet() * 2)
                .game(game)
                .currentPlayer(Piece.PLAYER_1)
                .isWinFlag(false)
                .build();

        userService.changeBalance(principal.getName(), -initializationRequest.bet());

        return connect4GameRepository.save(connect4Game);
    }

    public Connect4Game move(MoveRequest moveRequest) {
        Connect4Game currentGame = connect4GameRepository.findById(moveRequest.gameId())
                .orElseThrow(() -> new NoSuchElementException("Game not found!"));
        Connect4 currentGameObject = currentGame.getGame();

        boolean isWin = currentGameObject.move(moveRequest.columnIndex(), currentGame.getCurrentPlayer());
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
            userService.changeBalance(game.getFirstPlayerUsername(), game.getBetSum());
        }
        else {
            userService.changeBalance(game.getSecondPlayerUsername(), game.getBetSum());
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

    public List<GameDto> getAllGames() {
        List<Connect4Game> allGames = connect4GameRepository.findAll();

        return allGames.stream()
                .filter(game -> game.getSecondPlayerUsername().isEmpty())
                .map(gameMapper::toDto)
                .toList();
    }

    public Connect4Game join(JoinRequest joinRequest, Principal principal) {
        Connect4Game game = connect4GameRepository.findById(joinRequest.gameId())
                .orElseThrow(() -> new NoSuchElementException("Game not found!"));

        userService.changeBalance(principal.getName(), -(game.getBetSum() / 2));

        game = Connect4Game.builder()
                .secondPlayerUsername(principal.getName())
                .build();

        return connect4GameRepository.save(game);
    }
}

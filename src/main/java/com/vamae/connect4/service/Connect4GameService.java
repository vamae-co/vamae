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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class Connect4GameService {

    private final Connect4GameRepository connect4GameRepository;
    private final UserService userService;
    private final GameMapper gameMapper;

    public String init(InitializationRequest initializationRequest, Principal principal) {
        List<List<Piece>> columns = new ArrayList<>();
        for(int i = 0; i < initializationRequest.columns(); i++) {
            columns.add(new ArrayList<>());
        }

        Connect4 game = new Connect4(
                new GameBoardController(
                        new GameBoard(columns, initializationRequest.rows())
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

        connect4GameRepository.save(connect4Game);

        return connect4Game.getId();
    }

    public Connect4Game move(MoveRequest moveRequest) {
        Connect4Game currentGame = getGameById(moveRequest.gameId());
        if(currentGame.getSecondPlayerUsername() == null) {
            throw new NullPointerException("First player cannot make moves until the second player has joined the game");
        }

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

    public Connect4Game getGameById(String id) {
        return connect4GameRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Game not found!"));
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
                .filter(game -> game.getSecondPlayerUsername() == null)
                .map(gameMapper::toDto)
                .toList();
    }

    public Connect4Game join(JoinRequest joinRequest, Principal principal) {
        Connect4Game game = getGameById(joinRequest.gameId());

        userService.changeBalance(principal.getName(), -(game.getBetSum() / 2));

        game = Connect4Game.builder()
                .secondPlayerUsername(principal.getName())
                .build();

        return connect4GameRepository.save(game);
    }
}

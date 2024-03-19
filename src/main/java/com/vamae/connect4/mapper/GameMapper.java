package com.vamae.connect4.mapper;

import com.vamae.authorization.service.UserService;
import com.vamae.connect4.entity.Connect4Game;
import com.vamae.connect4.model.dto.GameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameMapper {

    private final UserService userService;

    public GameDto toDto(Connect4Game game) {
        return GameDto.builder()
                .gameId(game.getId())
                .firstPlayerUsername(userService.findUserById(game.getFirstPlayerId()).getUsername())
                .bet(game.getBetSum() / 2)
                .build();
    }
}

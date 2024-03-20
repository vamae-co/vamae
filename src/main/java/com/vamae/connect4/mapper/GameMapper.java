package com.vamae.connect4.mapper;

import com.vamae.connect4.entity.Connect4Game;
import com.vamae.connect4.model.dto.GameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameMapper {

    public GameDto toDto(Connect4Game game) {
        return GameDto.builder()
                .gameId(game.getId())
                .firstPlayerUsername(game.getFirstPlayerUsername())
                .bet(game.getBetSum() / 2)
                .build();
    }
}

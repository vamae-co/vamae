package com.vamae.poker.service.mappers;

import com.vamae.poker.lib.models.dto.PlayerDto;
import com.vamae.poker.model.responses.PlayerResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerMapper {
    public PlayerResponse toResponse(PlayerDto playerDto) {
        return PlayerResponse.builder()
                .id(playerDto.id())
                .chips(playerDto.chips())
                .currentBet(playerDto.currentBet())
                .build();
    }

    public List<PlayerResponse> toResponse(List<PlayerDto> players) {
        return players.stream()
                .map(this::toResponse)
                .toList();
    }
}

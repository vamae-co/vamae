package com.vamae.poker.lib.services.mappers;

import com.vamae.poker.lib.models.dto.TableDto;
import com.vamae.poker.lib.services.Table;
import com.vamae.poker.lib.services.states.*;

public class TableMapper {
    public static TableDto toDto(Table table) {
        return TableDto.builder()
                .settings(table.getSettings())
                .players(PlayerMapper.toDto(table.getPlayers()))
                .deck(table.getDeck())
                .currentBet(table.getCurrentBet())
                .pot(table.getPot())
                .cards(table.getCards())
                .state(table.getState().toString())
                .currentPlayerIndex(table.getCurrentPlayerIndex())
                .lastPlayerId(table.getState().getLastPlayerId())
                .build();
    }

    public static Table toTable(TableDto table) {
        Table result = Table.builder()
                .settings(table.settings())
                .deck(table.deck())
                .currentBet(table.currentBet())
                .pot(table.pot())
                .cards(table.cards())
                .build();

        result.setPlayers(PlayerMapper.toPlayer(table.players(), result));

        GameState state = fromString(table.state(), result);
        state.setLastPlayerId(table.lastPlayerId());
        result.setState(state);

        result.setCurrentPlayerIndex(table.currentPlayerIndex());

        return result;
    }

    private static GameState fromString(String state, Table table) {
        return switch (state) {
            case "PreFlop" -> new PreFlopState(table);
            case "Flop" -> new FlopState(table);
            case "Turn" -> new TurnState(table);
            case "River" -> new RiverState(table);
            case "Showdown" -> new ShowdownState(table);
            default -> new WaitingState(table);
        };
    }
}

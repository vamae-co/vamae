package com.vamae.poker.lib.controllers;

import com.vamae.poker.lib.models.dto.TableDto;
import com.vamae.poker.lib.models.records.Settings;
import com.vamae.poker.lib.services.Table;
import com.vamae.poker.lib.services.mappers.TableMapper;

public class Poker {
    public static TableDto create(Settings settings) {
        return TableMapper.toDto(new Table(settings));
    }

    public static TableDto join(TableDto tableDto) {
        Table table = TableMapper.toTable(tableDto);
        table.join();
        return TableMapper.toDto(table);
    }

    public static TableDto start(TableDto tableDto) {
        Table table = TableMapper.toTable(tableDto);
        table.start();
        return TableMapper.toDto(table);
    }

    public static TableDto end(TableDto tableDto) {
        Table table = TableMapper.toTable(tableDto);
        table.end();
        return TableMapper.toDto(table);
    }

    public static TableDto check(TableDto tableDto) {
        Table table = TableMapper.toTable(tableDto);
        table.check();
        return TableMapper.toDto(table);
    }

    public static TableDto call(TableDto tableDto) {
        Table table = TableMapper.toTable(tableDto);
        table.call();
        return TableMapper.toDto(table);
    }

    public static TableDto bet(TableDto tableDto, int amount) {
        Table table = TableMapper.toTable(tableDto);
        table.bet(amount);
        return TableMapper.toDto(table);
    }

    public static TableDto fold(TableDto tableDto) {
        Table table = TableMapper.toTable(tableDto);
        table.fold();
        return TableMapper.toDto(table);
    }

    public static TableDto raise(TableDto tableDto, int callAndRaise) {
        Table table = TableMapper.toTable(tableDto);
        table.raise(callAndRaise);
        return TableMapper.toDto(table);
    }
}

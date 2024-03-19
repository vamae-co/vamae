package com.vamae.connect4.lib.entity;

import lombok.Getter;
import com.vamae.connect4.lib.enums.Piece;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents game board
 */
@Getter
public class GameBoard {
    private final List<List<Piece>> columns;
    private final int rows;

    public GameBoard(int columns, int rows) {
        this.columns = new ArrayList<>();
        for(int i = 0; i < columns; i++) {
            this.columns.add(new ArrayList<>());
        }
        this.rows = rows;
    }

    /**
     * Getter for columns count
     * @return count of columns
     */
    public int getColumnsCount() {
        return columns.size();
    }
}

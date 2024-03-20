package com.vamae.connect4.lib.entity;

import lombok.Getter;
import com.vamae.connect4.lib.enums.Piece;

import java.util.List;

/**
 * Class that represents game board
 */
@Getter
public record GameBoard(
        List<List<Piece>> columns,
        int rows
) {
    /**
     * Getter for columns count
     *
     * @return count of columns
     */
    public int getColumnsCount() {
        return columns.size();
    }
}

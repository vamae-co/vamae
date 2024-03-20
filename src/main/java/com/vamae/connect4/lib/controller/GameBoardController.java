package com.vamae.connect4.lib.controller;

import com.vamae.connect4.lib.entity.GameBoard;
import com.vamae.connect4.lib.enums.Piece;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.vamae.connect4.lib.exceptions.PieceOutOfBoardException;

import java.util.List;

/**
 * The controller for the game's board.
 * @author Mykhail Varych
 */
@AllArgsConstructor
public class GameBoardController {

    @Getter
    private GameBoard gameBoard;

    /**
     * Check if piece is in board
     * @param x x coordinates of piece
     * @param y y coordinates of piece
     * @return coordinates of piece
     */
    public boolean isInBoard(int x, int y) {
        return (x >= 0 && x < gameBoard.getColumnsCount()) && (y >= 0 && y < gameBoard.rows());
    }

    /**
     * Gets the cell where piece was put
     * @param x x coordinates of piece
     * @param y y coordinates of piece
     * @return row
     * @throws PieceOutOfBoardException exception if piece is out of board
     */
    public Piece getCell(int x, int y) {
        if (isInBoard(x, y)) {
            List<Piece> column = gameBoard.columns().get(x);

            if (column.size() > y) {
                return column.get(y);
            } else {
                return null;
            }
        }
        else {
            throw new PieceOutOfBoardException("Piece is out of board!");
        }
    }
}

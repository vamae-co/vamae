package com.vamae.connect4.lib.controller;

import lombok.AllArgsConstructor;
import com.vamae.connect4.lib.enums.Piece;
import com.vamae.connect4.lib.exceptions.PieceOutOfBoardException;

import java.util.List;

/**
 * The controller for game
 */
@AllArgsConstructor
public class Connect4 {
    private GameBoardController gameBoardController;

    /**
     * Method for making a move
     * @param x index of column in which player want to drop a piece
     * @param player player, who moves
     * @return true if player win, otherwise false
     * @throws PieceOutOfBoardException if piece is out of bound
     * @throws IllegalArgumentException if column is full
     */
    public boolean move(int x, Piece player) {
        if(gameBoardController.isInBoard(x, 0)) {
            List<Piece> column = gameBoardController.getGameBoard().columns().get(x);

            if (column.size() >= gameBoardController.getGameBoard().rows()) {
                throw new IllegalArgumentException("That column is full");
            }

            column.add(player);

            return checkWin(x, column.size() - 1, player);
        }
        throw new PieceOutOfBoardException("Piece is out of board!");
    }

    /**
     * Method that checks if line is a win line
     * @param startX starting x coordinates
     * @param startY starting y coordinates
     * @param endX x direction (should be -1, 0, 1)
     * @param endY y direction (should be -1, 0, 1)
     * @param player the player for whom the line is checked
     * @return true if continuous line belongs to player, false if line is out of board or doesn't belong to player
     */
    private boolean checkLine(int startX, int startY, int endX, int endY, Piece player) {
        for(int i = 0; i < (gameBoardController.getGameBoard().getColumnsCount() + 1) / 2; i++) {
            int checkedX = startX + (endX * i);
            int checkedY = startY + (endY * i);

            if(!gameBoardController.isInBoard(checkedX, checkedY)) {
                return false;
            }
            else if(player != gameBoardController.getCell(checkedX, checkedY)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method that checks win combination for player
     * @param x x coordinates where piece is
     * @param y y coordinates where piece is
     * @param player player, who places piece
     * @return true if player wins, otherwise false
     */
    private boolean checkWin(int x, int y, Piece player) {
        int piecesForWinCount = (gameBoardController.getGameBoard().getColumnsCount() + 1) / 2;
        if (checkLine(x, y, 0, -1, player)) {
            return true;
        }

        for(int offset = 0; offset < piecesForWinCount; offset++) {
            if(checkLine(x - (piecesForWinCount - 1) + offset, y, 1, 0, player)) {
                return true;
            }

            if(checkLine(x - (piecesForWinCount - 1) + offset, y + (piecesForWinCount - 1) - offset, 1, -1, player)) {
                return true;
            }

            if(checkLine(x - (piecesForWinCount - 1) + offset, y - (piecesForWinCount - 1) + offset, 1, 1, player)) {
                return true;
            }
        }
        return false;
    }
}

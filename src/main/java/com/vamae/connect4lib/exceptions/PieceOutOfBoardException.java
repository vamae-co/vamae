package com.vamae.connect4lib.exceptions;

/**
 * Exception if piece is out of board
 */
public class PieceOutOfBoardException extends ArrayIndexOutOfBoundsException{
    /**
     * Method for calling the exception
     * @param message message string
     */
    public PieceOutOfBoardException(String message) {
        super(message);
    }
}

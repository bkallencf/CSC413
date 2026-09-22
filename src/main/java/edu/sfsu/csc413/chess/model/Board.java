package edu.sfsu.csc413.chess.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int BOARD_SIZE = 8;     // A chess board is 8x8, but could potentially be adapted to handle shogi
    private Piece[][] pieces;                   // (file, rank) from (0, 0) = a1 to (7, 7) = h8

    public Board() {
        this.pieces = new Piece[BOARD_SIZE][BOARD_SIZE];
    }

    public Piece pieceAt(Position square) {
        // Position already enforces a valid square on the board, thus we don't have to check for it
        return this.pieces[square.file()][square.rank()];
    }

    public boolean isEmpty(Position square) {
        // Position already enforces a valid square on the board, thus we don't have to check for it
        return this.pieces[square.file()][square.rank()] == null;
    }

    public void place(Position square, Piece piece) {
        try {
            this.pieces[square.file()][square.rank()] = piece;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Position or Piece", e);
        }
    }

    public List<Position> positionsOf(Color pieceColor) {
        ArrayList<Position> colorPositions = new ArrayList<>();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (this.pieces[i][j] != null && this.pieces[i][j].color().equals(pieceColor)) {
                    colorPositions.add(new Position(i, j));
                }
            }
        }

        return colorPositions;
    }

    @Override
    public String toString() {
        String FENString = "";

        // Cycles through the rank starting at the top left corner
        for (int i = BOARD_SIZE - 1; i >= 0; i--) {
            int emptyPositions = 0;
            // Cycles through the file
            for (int j = 0; j < BOARD_SIZE; j++) {
                // Look for the pieces and count the empty positions before/between them
                if (this.pieces[j][i] != null) {
                    if (emptyPositions != 0) {
                        FENString += emptyPositions;
                    }

                    FENString += pieces[j][i].symbol();
                    emptyPositions = 0;
                } else {
                    emptyPositions++;
                }
            }

            // Check if there are any final spots after the pieces
            if (emptyPositions != 0) {
                FENString += emptyPositions;
            }

            if (i != 0) {
                FENString += "/";
            }
        }

        return FENString;
    }
}
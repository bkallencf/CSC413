package edu.sfsu.csc413.chess.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int BOARD_SIZE = 8;     // A chess board is 8x8, but could potentially be adapted to handle shogi
    private Piece[][] pieces;                   // (file, rank) from (0, 0) = a1 to (7, 7) = h8

    // Constructors
    public Board() {
        this.pieces = new Piece[BOARD_SIZE][BOARD_SIZE];
    }

    // Getters
    public Piece pieceAt(Position square) {
        // Position already enforces a valid square on the board, thus we don't have to check for it
        return this.pieces[square.file()][square.rank()];
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

    /** 
     * Returns the position on the board of the king of the given color.
     */
    public Position kingPosition(Color color) {
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[0].length; j++) {
                if (pieces[i][j] == null) {
                    continue;
                }
                if (pieces[i][j].type() == PieceType.KING && pieces[i][j].color() == color) {
                    return new Position(i, j);
                }
            }
        }
        return null;
    }

    // Setters
    public void place(Position square, Piece piece) {
        try {
            this.pieces[square.file()][square.rank()] = piece;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Position or Piece", e);
        }
    }

    // Other Functions
    /**
     * Checks if a given board position doesn't have a piece on it.
     */
    public boolean isEmpty(Position square) {
        // Position already enforces a valid square on the board, thus we don't have to check for it
        return this.pieces[square.file()][square.rank()] == null;
    }

    /**
     * Given a move, it applies whatever the move says happened.
     * Checks if the given move was a promotion.
     * If it was a capture, the piece simply no longer exists on the board.
     * Trusts that the given Move is valid.
     */
    public void apply(Move move) {
        Piece pieceMoved;
        // Boolean check for promotion over using default to avoid checking the entire switch on every move
        if (move.isPromotion()) {
            // Ordered from most to least likely to be promoted to for minor performance increase
            switch(move.promotesTo()) {
                case PieceType.QUEEN -> pieceMoved = new Queen(move.moved().color());
                case PieceType.ROOK -> pieceMoved = new Rook(move.moved().color());
                case PieceType.BISHOP -> pieceMoved = new Bishop(move.moved().color());
                case PieceType.KNIGHT -> pieceMoved = new Knight(move.moved().color());
                // Redundant because if isPromotion() is true, promotesTo() will never be a King or Pawn
                default -> pieceMoved = null;
            }
        } else {
            pieceMoved = move.moved();
        }
        this.pieces[move.to().file()][move.to().rank()] = pieceMoved;
        this.pieces[move.from().file()][move.from().rank()] = null;
    }

    /**
     * Undoes the given move whether it was a promotion, attack, or a regular move.
     * If it was an attack, places the captured piece back.
     */
    public void undo(Move move) {
        this.pieces[move.from().file()][move.from().rank()] = move.moved();
        if (move.isCapture()) {
            this.pieces[move.to().file()][move.to().rank()] = move.captured();
        } else {
            this.pieces[move.to().file()][move.to().rank()] = null;
        }
    }

    // Overriden Functions
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
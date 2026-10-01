package edu.sfsu.csc413.chess.model;

import java.util.List;
import java.util.ArrayList;

public abstract class Piece {
    private final Color color;
    private final PieceType pieceType;

    // Constructors
    protected Piece(Color color, PieceType pieceType) {
        this.color = color;
        this.pieceType = pieceType;
    }

    // Getters
    public Color color() {
        return this.color;
    }

    public PieceType type() {
        return this.pieceType;
    }

    /**
     * Returns the type of the Piece as defined within PieceType
     */
    public char symbol() {
        // Lowercase = BLACK, Uppercase = WHITE
        try {
            return (this.color.equals(Color.WHITE)) ? this.pieceType.symbol() : Character.toLowerCase(this.pieceType.symbol());
        } catch (NullPointerException e) {
            throw new NullPointerException("Color was not properly initialized");
        }
    }

    // Other Functions
    /** 
     * Defines all the potential moves a piece can make, regardlss of actual validity
     */
    public abstract List<Move> pseudoLegalMoves(Board board, Position from);
    
    /**
     * Sliding moves until it hits the edge of the board or another piece; checks validity
     */
    protected List<Move> slidingMoves(Board board, Position from, int[][] directions) {
        ArrayList<Move> moves = new ArrayList<>();
        for (int[] direction : directions) {
            int i = 0;
            // Will go until it runs off the board
            try {
                Position to = new Position(from.file() + direction[0], from.rank() + direction[1]);
                // Checks the squares of the board that are empty
                while(board.pieceAt(to) == null) {
                    moves.add(Move.quiet(from, to, board.pieceAt(from)));
                    // Increment to the next position
                    i++;
                    to = new Position(from.file() + direction[0] * i, from.rank() + direction[1] * i);
                }
                // Checks if the last square has an enemy piece which can be captured
                if (board.pieceAt(to).color() != board.pieceAt(from).color()) {
                    moves.add(Move.capture(from, to, board.pieceAt(from), board.pieceAt(to)));
                }
            // Doesn't do anything with the exception, keeps going to the next direction
            } catch (IllegalArgumentException e) {}
        }

        return moves;
    }

    /**
     * Stepping moves 1 in the given directions; doesn't check validity
     * Doesn't check for promotion because only Pawn cares about that
     */
    protected List<Move> steppingMoves(Board board, Position from, int[][] directions) {
        ArrayList<Move> moves = new ArrayList<>();
        for (int[] direction : directions) {
            // Double checks the move is on the board
            try {
                Position to = new Position(from.file() + direction[0], from.rank() + direction[1]);
                if (board.pieceAt(to) != null) {
                    if (board.pieceAt(from).color() != board.pieceAt(to).color()) {
                        moves.add(Move.capture(from, to, board.pieceAt(from), board.pieceAt(to)));
                    }
                } else {
                    moves.add(Move.quiet(from, to, board.pieceAt(from)));
                }
            // Doesn't do anything with the exception, just prevents the program from stopping if the move is off the board
            } catch (IllegalArgumentException e) {}
        }

        return moves;
    }

    /**
     * Checks the space to see if the move attacks another piece
     * Can be used to help define pawn movement and use 'x' chess notation
     */
    public boolean attacks(Board board, Position from, Position target) {
        for (Move move : board.pieceAt(from).pseudoLegalMoves(board, from)) {
            if (move.to().equals(target)) {
                return true;
            }
        }

        return false;
    }

    // Overriden Functions
    @Override
    public String toString() {
        return "" + this.symbol();
    }
}

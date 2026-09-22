package edu.sfsu.csc413.chess.model;

public class Piece {
    private final Color color;
    private final PieceType pieceType;

    protected Piece(Color color, PieceType pieceType) {
        this.color = color;
        this.pieceType = pieceType;
    }

    public char symbol() {
        // Lowercase = BLACK, Uppercase = WHITE
        try {
            return (this.color.equals(Color.WHITE)) ? this.pieceType.symbol() : Character.toLowerCase(this.pieceType.symbol());
        } catch (NullPointerException e) {
            throw new NullPointerException("Color was not properly initialized");
        }
    }

    public Color color() {
        return this.color;
    }

    public PieceType type() {
        return this.pieceType;
    }

    @Override
    public String toString() {
        return "" + this.symbol();
    }
}

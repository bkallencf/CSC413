package edu.sfsu.csc413.chess.model;

public enum PieceType {
    PAWN('P'), KNIGHT('N'), BISHOP('B'), ROOK('R'), QUEEN('Q'), KING('K');

    private final char symbol;

    PieceType(char symbol) {
        this.symbol = symbol;
    }

    public char symbol() {
        return this.symbol;
    }

    public static PieceType fromSymbol(char symbol) {
        PieceType originalType = null;
        switch(Character.toUpperCase(symbol)) {
            case 'P' -> originalType = PieceType.PAWN;
            case 'N' -> originalType = PieceType.KNIGHT;
            case 'B' -> originalType = PieceType.BISHOP;
            case 'R' -> originalType = PieceType.ROOK;
            case 'Q' -> originalType = PieceType.QUEEN;
            case 'K' -> originalType = PieceType.KING;
            default -> throw new IllegalArgumentException("Symbol does not correspond to any Piece: symbol=" + symbol);
        }

        return originalType;
    }
    
}

package edu.sfsu.csc413.chess.model;

public record Position(int file, int rank) {
    public static final int BOARD_SIZE = 8;         // A chess board is 8x8, but could potentially be adapted to handle shogi

    public Position {
        if (!isOnBoard(file, rank)) {
            throw new IllegalArgumentException("Position off board: file=" + file + ", rank=" + rank);
        }
    }

    private static boolean isOnBoard(int file, int rank) {
        return (file >= 0 && rank >= 0 && file < BOARD_SIZE && rank < BOARD_SIZE);
    }

    public static Position parse(String coordinate) {
        if (coordinate.length() > 2) {
            throw new IllegalArgumentException("Invalid Position: given argument is too long, must specify rank and file");
        } else if (coordinate.length() < 2) {
            throw new IllegalArgumentException("Invalid Position: given argument contains too few characters, must specify rank and file");
        }

        // e2 = e - file, 2 - rank
        // Subtract ASCII values to create valid positions on our 0-indexed board; 'a' = 97, '1' = 49
        int coordFile = coordinate.charAt(0) - 'a';
        int coordRank = coordinate.charAt(1) - '1';

        if (!isOnBoard(coordFile, coordRank)) {
            throw new IllegalArgumentException("Invalid Position: Position lies off of the board" + coordinate);
        }

        return new Position(coordFile, coordRank);
    }

    public Position offsetOrNull(int xMove, int yMove) {
        int newFile = this.file + xMove;
        int newRank = this.rank + yMove;

        if (!isOnBoard(newFile, newRank)) {
            return null;
        }

        return new Position(newFile, newRank);
    }

    @Override
    public String toString() {
        return "" + (char)('a' + file) + (char)('1' + rank);
    }
}

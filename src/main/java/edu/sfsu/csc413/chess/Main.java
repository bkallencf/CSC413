package edu.sfsu.csc413.chess;

import edu.sfsu.csc413.chess.model.*;
import edu.sfsu.csc413.chess.view.*;

/**
 * Entry point.
 *
 * <p>At M0 this does nothing but prove the toolchain works. It grows into the
 * real launcher as the engine appears underneath it.
 */
public final class Main {

    public static void main(String[] args) {
        System.out.println("CSC 413 Chess — environment OK.");
        
        Board board = setupStartingPosition();
        System.out.println(new TextBoardRenderer(PieceGlyphs.LETTERS).render(board));
    }

    private Main() {
    }

    // Helper method to build the starting position
    private static Board setupStartingPosition() {
        Piece[] firstRank = {new Rook(Color.WHITE), new Knight(Color.WHITE), new Bishop(Color.WHITE), new Queen(Color.WHITE), new King(Color.WHITE), new Bishop(Color.WHITE), new Knight(Color.WHITE), new Rook(Color.WHITE)};
        Piece[] backRank = {new Rook(Color.BLACK), new Knight(Color.BLACK), new Bishop(Color.BLACK), new Queen(Color.BLACK), new King(Color.BLACK), new Bishop(Color.BLACK), new Knight(Color.BLACK), new Rook(Color.BLACK)};

        Board startingPosition = new Board();
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            // Place white pieces and pawns
            startingPosition.place(new Position(i, 0), firstRank[i]);
            startingPosition.place(new Position(i, 1), new Pawn(Color.WHITE));
            // Place black pieces and pawns
            startingPosition.place(new Position(i, 7), backRank[i]);
            startingPosition.place(new Position(i, 6), new Pawn(Color.BLACK));
        }

        return startingPosition;
    }
}
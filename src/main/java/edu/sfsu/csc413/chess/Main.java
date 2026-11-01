package edu.sfsu.csc413.chess;

import edu.sfsu.csc413.chess.model.*;
import edu.sfsu.csc413.chess.view.*;
import edu.sfsu.csc413.chess.factory.*;
import edu.sfsu.csc413.chess.engine.*;

/**
 * Entry point.
 *
 * <p>At M0 this does nothing but prove the toolchain works. It grows into the
 * real launcher as the engine appears underneath it.
 */
public final class Main {

    public static void main(String[] args) {
        Game chess = new Game();
        TextBoardRenderer renderer = new TextBoardRenderer(PieceGlyphs.LETTERS);
        System.out.println(renderer.render(chess.board()));
        System.out.println();

        String[] moves = {"f2f3", "e7e5", "g2g4", "d8h4"};
        for (String notation : moves) {
            chess.play(chess.findLegalMove(notation).orElseThrow());
            System.out.println(renderer.render(chess.board()));
            System.out.println();
        }
        System.out.println(chess.legalMoves());
    }

    private Main() {
    }
}
package edu.sfsu.csc413.chess;

import edu.sfsu.csc413.chess.model.*;
import edu.sfsu.csc413.chess.view.*;
import edu.sfsu.csc413.chess.factory.*;
import edu.sfsu.csc413.chess.engine.*;

import java.util.Scanner;

/**
 * Entry point.
 *
 * <p>At M0 this does nothing but prove the toolchain works. It grows into the
 * real launcher as the engine appears underneath it.
 */
public final class Main {

    public static void main(String[] args) {
        Game chess = new Game();
        System.out.print("Choose a view (emoji, console, swing, pgn): ");
        Scanner in = new Scanner(System.in);

        BoardView view;
        switch(in.nextLine()) {
            case "emoji" -> { view = new EmojiView(); }
            case "console" -> { view = new ConsoleView(); }
            case "swing" -> { view = new SwingView(); }
            case "pgn" -> { view = new PGNView(); }
            default -> { view = new ConsoleView(); }
        }

        new GameController(chess, view).start();
        in.close();
    }

    private Main() {
    }
}
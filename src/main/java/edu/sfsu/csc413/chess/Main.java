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
        System.out.print("Choose a view (emoji, console): ");
        Scanner in = new Scanner(System.in);

        BoardView view;
        switch(in.nextLine()) {
            case "emoji" -> { view = new EmojiView(); }
            case "console" -> { view = new ConsoleView(); }
            default -> { view = new ConsoleView(); }
        }

        new GameController(chess, view).start();
    }

    private Main() {
    }
}
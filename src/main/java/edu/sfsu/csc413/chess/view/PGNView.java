package edu.sfsu.csc413.chess.view;

import edu.sfsu.csc413.chess.engine.Game;
import edu.sfsu.csc413.chess.model.Color;
import edu.sfsu.csc413.chess.model.PieceType;
import edu.sfsu.csc413.chess.model.Move;
import edu.sfsu.csc413.chess.model.Position;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class PGNView implements BoardView {
    private final PrintStream out;
    private final Scanner in;
    private final Color perspective;
    
    public PGNView() {
        this(System.out, new Scanner(System.in), Color.WHITE);
    }

    public PGNView(PrintStream out, Scanner in, Color perspective) {
        this.out = out;
        this.in = in;
        this.perspective = perspective;
    }

    @Override
    public void showMessage(String message) {
        out.println(message);
    }

    @Override
    public void render(Game game) {
        out.println();
        if (this.perspective == Color.WHITE) {
            for (int i = Position.BOARD_SIZE - 1; i >= 0; i--) {
                out.println(("+---".repeat(Position.BOARD_SIZE)) + "+");
                for (int j = 0; j < Position.BOARD_SIZE; j++) {
                    String symbol = PieceGlyphs.LETTERS.glyphFor(game.board().pieceAt(new Position(j, i)));
                    symbol = (symbol.equals(".")) ? " " : symbol;
                    out.print("| " + symbol + " ");
                }
                out.println("|");
            }
        } else {
            for (int i = 0; i < Position.BOARD_SIZE; i++) {
                out.println(("+---".repeat(Position.BOARD_SIZE)) + "+");
                for (int j = Position.BOARD_SIZE - 1; j >= 0; j--) {
                    String symbol = PieceGlyphs.LETTERS.glyphFor(game.board().pieceAt(new Position(j, i)));
                    symbol = (symbol.equals(".")) ? " " : symbol;
                    out.print("| " + symbol + " ");
                }
                out.println("|");
            }
        }
        out.println(("+---".repeat(Position.BOARD_SIZE)) + "+");
        out.println();
        out.println(game.sideToMove() + " to move  |  " + game.status());
    }

    @Override
    public void start(GameController controller) {
        controller.runLoop();
    }

    @Override
    public PlayerAction nextAction(Game game) {
        out.print("Your move (e.g. e4, Nf3, 'undo', 'moves', 'quit'): ");
        out.flush();
        String action = (in.hasNextLine()) ? in.nextLine() : "quit";
        if (action.equals("undo") || action.equals("moves") || action.equals("quit") || action.length() < 2) {
            return PlayerAction.parse(action);
        }

        PieceType type;
        int destinationStart = 0;
        switch (action.charAt(0)) {
            case 'N' -> { type = PieceType.KNIGHT; destinationStart++; }
            case 'B' -> { type = PieceType.BISHOP; destinationStart++; }
            case 'R' -> { type = PieceType.ROOK; destinationStart++; }
            case 'Q' -> { type = PieceType.QUEEN; destinationStart++; }
            case 'K' -> { type = PieceType.KING; destinationStart++; }
            default -> { type = PieceType.PAWN; }
        }
        
        boolean captures = (action.length() >= 2 && action.charAt(1) == 'x');
        if (captures) {
            destinationStart++;
            if (type == PieceType.PAWN) {
                destinationStart++;
            }
        }
        char promotes = ' ';
        if (type == PieceType.PAWN) {
            if (captures && action.length() == 6 && action.charAt(4) == '=') {
                promotes = action.charAt(5);
            } else if (action.length() == 4 && action.charAt(2) == '=') {
                promotes = action.charAt(3);
            }
        }

        try {
            List<Move> legalMoves = game.legalMoves();
            Move found = null;
            // Doesn't deal with disambiguation, simply takes the first thing it finds
            for (Move move : legalMoves) {
                // First filtration pass made based on destination and piece type
                if (move.to().equals(Position.parse(action.substring(destinationStart, destinationStart + 2))) && move.moved().type() == type) {
                    // Fixed a bug where pawn captures ignored the first letter (dxd4 = exd4 = jxd4 --> valid)
                    if (type == PieceType.PAWN && move.from().file() != (action.charAt(0) - 'a')) {
                        continue;
                    }
                    
                    // Second filtration based on promotion and attacking
                    if (promotes != ' ' && move.promotesTo() == PieceType.fromSymbol(promotes)) {
                        found = move;
                        break;
                    } else if (captures && move.isCapture()) {
                        found = move;
                        break;
                    } else if (promotes == ' ' && !captures) {
                        found = move;
                        break;
                    }
                }
            }
    
            String moveAction = (found != null) ? found.from().toString() + found.to().toString() : action;
    
            return PlayerAction.parse(moveAction);
        } catch (IllegalArgumentException e) {
            return PlayerAction.parse(action);
        }
    }
}

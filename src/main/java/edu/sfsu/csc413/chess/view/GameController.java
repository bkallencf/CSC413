package edu.sfsu.csc413.chess.view;

import edu.sfsu.csc413.chess.engine.Game;
import edu.sfsu.csc413.chess.engine.GameStatus;
import edu.sfsu.csc413.chess.model.Move;
import edu.sfsu.csc413.chess.model.PieceType;

import java.util.Optional;
import java.util.List;

/**
 * Connects the player's decisions to the game: carry out what was asked, then
 * ask the view to redraw.
 *
 * <p>This is the C in MVC. It is the only class that knows about both the
 * {@link Game} and the {@link BoardView}. It holds no game state of its own:
 * the model owns the position, the view owns the display, and the controller
 * owns only the conversation between them.
 *
 * <p>When you are done, this class mentions no {@code Scanner}, no
 * {@code System.out}, no window, and none of the words "undo", "moves", or
 * "quit". Those belong to the view and to {@link PlayerAction#parse}.
 */
public class GameController {

    private final Game game;
    private final BoardView view;

    public GameController(Game game, BoardView view) {
        this.game = game;
        this.view = view;
    }

    /** The game being played, so a view can read it in order to draw it. */
    public Game game() {
        return game;
    }

    /**
     * Greets the player, draws the opening position, and hands control to the
     * view with {@link BoardView#start}.
     */
    public void start() {
        view.showMessage("Welcome to CSC 413 Chess Engine.");
        view.render(game);
        view.start(this);
    }

    /**
     * The ask-and-handle loop, for views that can wait for input.
     *
     * <p>Asks the view for the next action and handles it, until the player
     * quits or the game is over. A console view calls this from its
     * {@code start}; an event-driven view never calls it.
     */
    public void runLoop() {
        PlayerAction nextAction = this.view.nextAction(game);
        while (nextAction != null && !nextAction.equals(new PlayerAction.Quit())) {
            this.handle(nextAction);
            // Exits prematurely if the game is ended
            if (this.game.isGameOver()) {
                break;
            }

            // Get the players next action and continue
            nextAction = this.view.nextAction(game);
        }
        // Handles the final quit action
        this.handle(new PlayerAction.Quit());
    }

    /**
     * Carries out one player action and redraws.
     *
     * <p>Every route into the game goes through here: {@link #runLoop()} calls
     * it, and so will a button's click handler in M7. The handout lists what
     * each of the four actions does and what the player is told.
     */
    public void handle(PlayerAction action) {
        switch (action) {
            // Lets the player undo their move
            case PlayerAction.Undo Undo -> {
                Optional<Move> move = game.undoLastMove();
                if (move.isEmpty()) {
                    this.view.showMessage("Nothing to undo.");
                }
            }
            // Lists the moves available to the player
            case PlayerAction.ShowMoves ShowMoves -> {
                List<Move> availableMoves = this.game.legalMoves();
                String message = availableMoves.size() + "legal moves:";
                for (Move legalMove : availableMoves) {
                    message += legalMove.toString() + " ";
                }
                this.view.showMessage(message);
            }
            // Does the players move
            case PlayerAction.Move Move -> {
                PlayerAction.Move nextMove = (PlayerAction.Move) action;
                List<Move> legalMoves = this.game.legalMoves();
                boolean legal = false;
                for (Move move : legalMoves) {
                    if (move.toString().substring(0, 4).equals(nextMove.notation().substring(0, 4))) {
                        Move doMove = move;
                        if (move.isPromotion()) {
                            char promotesTo = (nextMove.notation().length() == 5) ? nextMove.notation().charAt(4) : ' ';
                            PieceType promotion;
                            switch (Character.toLowerCase(promotesTo)) {
                                case 'n' -> { promotion = PieceType.KNIGHT; }
                                case 'b' -> { promotion = PieceType.BISHOP; }
                                case 'r' -> { promotion = PieceType.ROOK; }
                                case 'q' -> { promotion = PieceType.QUEEN; }
                                default -> { promotion = this.view.promotionChoice(this.game); }
                            }
                            doMove = edu.sfsu.csc413.chess.model.Move.promotion(move.from(), move.to(), move.moved(), move.captured(), promotion);
                        }
                        
                        this.game.play(doMove);
                        legal = true;
                        break;
                    }
                }
                
                if (!legal) {
                    this.view.showMessage("Not a legal move: '" + nextMove.notation() + "'.");
                }
            }
            // Quit the game
            case PlayerAction.Quit Quit -> {
                this.view.showMessage("Goodbye.");
                this.view.close();
                return;
            }
        }

        this.view.render(this.game);
        if (this.game.isGameOver()) {
            if (this.game.status() == GameStatus.CHECKMATE) {
                this.view.showMessage("Checkmate. " + this.game.sideToMove().opposite() + " wins.");
            } else {
                this.view.showMessage("Stalemate. The game is a draw.");
            }
        }
    }
}

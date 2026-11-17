package edu.sfsu.csc413.chess.view;

import edu.sfsu.csc413.chess.engine.Game;
import edu.sfsu.csc413.chess.model.Color;
import edu.sfsu.csc413.chess.model.Piece;
import edu.sfsu.csc413.chess.model.Position;
import edu.sfsu.csc413.chess.model.Move;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

public class SwingView implements BoardView {
    private JFrame frame = new JFrame("CSC 413 Chess Engine");
    private final BoardPanel boardTiles;
    private final Color perspective;
    // Colors based on my chess.com board; backwards to render board colors correctly
    private final String[] colors = {"#b2c6d9", "#e9eef7"}; // a1, b1
    private final String[] highlightColors = {"#8c9dad", "#afb3ba"};
    private String nextMove = "";

    public SwingView() {
        this(Color.WHITE);
    }

    public SwingView(Color perspective) {
        this.perspective = perspective;

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(800, 800);
        this.frame.setLayout(new BorderLayout());
        // Stack Overflow: https://stackoverflow.com/questions/2442599/how-to-set-jframe-to-appear-centered-regardless-of-monitor-resolution
        this.frame.setLocationRelativeTo(null);

        this.boardTiles = new BoardPanel(colors, highlightColors, this.perspective);

        this.frame.add(boardTiles);
        // TODO: implement undo
        // this.frame.add(new JButton("Undo"), SwingConstants.CENTER);

        this.frame.setVisible(true);
    }

    @Override
    public void render(Game game) {
        this.frame.getGlassPane().setVisible(false);
        // Stack Overflow: https://stackoverflow.com/questions/51746451/how-do-i-make-a-jpanel-grid
        JPanel pieces = new JPanel(new GridLayout(Position.BOARD_SIZE, Position.BOARD_SIZE));
        if (perspective == Color.WHITE) {
            for (int i = Position.BOARD_SIZE - 1; i >= 0; i--) {                    // rank
                for (int j = 0; j < Position.BOARD_SIZE; j++) {                     // file
                    Piece currentPiece = game.board().pieceAt(new Position(j, i));
                    String pieceDisplay = (currentPiece == null) ? "" : PieceGlyphs.FIGURES.glyphFor(currentPiece);
                    JLabel currentPieceTile = new JLabel(pieceDisplay, SwingConstants.CENTER);
                    // Stack Overflow: https://stackoverflow.com/questions/2715118/how-to-change-the-size-of-the-font-of-a-jlabel-to-take-the-maximum-size
                    currentPieceTile.setFont(new Font("Serif", Font.PLAIN, 60));
                    pieces.add(currentPieceTile);
                }
            }
        } else {
            for (int i = 0; i < Position.BOARD_SIZE; i++) {                         // rank
                for (int j = Position.BOARD_SIZE - 1; j >= 0; j--) {                // file
                    Piece currentPiece = game.board().pieceAt(new Position(j, i));
                    String pieceDisplay = (currentPiece == null) ? "" : PieceGlyphs.FIGURES.glyphFor(currentPiece);
                    JLabel currentPieceTile = new JLabel(pieceDisplay, SwingConstants.CENTER);
                    // Stack Overflow: https://stackoverflow.com/questions/2715118/how-to-change-the-size-of-the-font-of-a-jlabel-to-take-the-maximum-size
                    currentPieceTile.setFont(new Font("Serif", Font.PLAIN, 60));
                    pieces.add(currentPieceTile);
                }
            }
        }
        pieces.setOpaque(false);
        this.frame.setGlassPane(pieces);
        pieces.setVisible(true);
    }

    @Override
    public void showMessage(String message) {
        JFrame popup = new JFrame("Chess Engine: Message");
        popup.setSize(400, 100);
        popup.setLayout(new BorderLayout());
        popup.add(new JLabel(message, SwingConstants.CENTER));
        popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        popup.setLocationRelativeTo(null);
        popup.setVisible(true);
    }

    @Override
    public void start(GameController controller) {
        for (int i = 0; i < Position.BOARD_SIZE; i++) {                     // rank
            for (int j = 0; j < Position.BOARD_SIZE; j++) {                 // file
                final int a = i;
                final int b = j;
                // Reddit: https://www.reddit.com/r/javahelp/comments/ayhd7w/how_do_i_create_a_clickable_panel_in_java_jpanel/
                this.boardTiles.boardPanels[j][i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (boardTiles.isHighlighted(b, a) && boardTiles.activeHighlightOrigin != null && !(boardTiles.activeHighlightOrigin[0] == b && boardTiles.activeHighlightOrigin[1] == a)) {
                            nextMove = new Position(boardTiles.activeHighlightOrigin[0], boardTiles.activeHighlightOrigin[1]) + new Position(b, a).toString();
                            boardTiles.activeHighlightOrigin = null;
                            boolean isPromotion = false;
                            String[] promotionChoices = {"n", "b", "r", "q"};
                            for (String choice : promotionChoices) {
                                if (!controller.game().findLegalMove(nextMove + choice).isEmpty()) {
                                    isPromotion = true;
                                    break;
                                }
                            }
    
                            if (isPromotion) {
                                askPromotion();
                            }

                            controller.handle(PlayerAction.parse(nextMove));
                        }
                        
                        if (!controller.game().board().isEmpty(new Position(b, a))) {
                            // Reset all the tiles so only the selected one is highlighted
                            for (int rank = 0; rank < Position.BOARD_SIZE; rank++) {
                                for (int file = 0; file < Position.BOARD_SIZE; file++) {
                                    if (boardTiles.isHighlighted(file, rank)) {
                                        boardTiles.switchColor(file, rank);
                                    }
                                }
                            }
                            boardTiles.switchColor(b, a);
                            boardTiles.activeHighlightOrigin = new int[] {b, a};
                            
                            // Highlight all the potential moves
                            List<Move> pieceMoves = controller.game().pieceLegalMoves(new Position(b, a));
                            boolean promotionHighlighted = false;
                            for (Move move : pieceMoves) {
                                if (!promotionHighlighted && move.isPromotion()) {
                                    promotionHighlighted = true;
                                    boardTiles.switchColor(move.to().file(), move.to().rank());
                                } else if (!move.isPromotion()) {
                                    boardTiles.switchColor(move.to().file(), move.to().rank());
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    public void askPromotion() {
        // Stack Overflow: https://stackoverflow.com/questions/2405039/pop-up-window-in-java-swing
        JDialog promotionWindow = new JDialog(this.frame, "Promotion!", true);
        promotionWindow.setSize(400, 100);
        promotionWindow.setLayout(new FlowLayout());
        promotionWindow.setLocationRelativeTo(null);

        List<JButton> promotionChoices = new ArrayList<>();
        JButton knightPromotion = new JButton("Knight");
        JButton bishopPromotion = new JButton("Bishop");
        JButton rookPromotion = new JButton("Rook");
        JButton queenPromotion = new JButton("Queen");

        promotionChoices.add(knightPromotion);
        promotionChoices.add(bishopPromotion);
        promotionChoices.add(rookPromotion);
        promotionChoices.add(queenPromotion);

        for (JButton choice : promotionChoices) {
            choice.setEnabled(true);
            choice.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Object obj = e.getSource();
                    if (obj  instanceof JButton) {
                        JButton button = (JButton) obj;
                        switch(button.getText()) {
                            case "Knight" -> { nextMove += "n"; }
                            case "Bishop" -> { nextMove += "b"; }
                            case "Rook" -> { nextMove += "r"; }
                            case "Queen" -> { nextMove += "q"; }
                            // No default case is needed, the user is clicking preset buttons
                        }
                        promotionWindow.dispose();
                    }
                }
            });
            promotionWindow.add(choice);
        }

        promotionWindow.setVisible(true);
    }
}
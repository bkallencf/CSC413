package edu.sfsu.csc413.chess.view;

import edu.sfsu.csc413.chess.model.Position;
import edu.sfsu.csc413.chess.model.Color;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    public final String[] colors;
    public final String[] highlightColors;
    public final JPanel[][] boardPanels = new PiecePanel[Position.BOARD_SIZE][Position.BOARD_SIZE];     // file, rank
    public int[] activeHighlightOrigin;

    public BoardPanel(String[] colors, String[] highlightColors, Color perspective) {
        this.colors = colors;
        this.highlightColors = highlightColors;
        
        // Stack Overflow: https://stackoverflow.com/questions/51746451/how-do-i-make-a-jpanel-grid
        this.setLayout(new GridLayout(Position.BOARD_SIZE, Position.BOARD_SIZE));       // rank, file
        // Cycle from a1 to h8
        if (perspective == Color.WHITE) {
            for (int i = Position.BOARD_SIZE - 1; i >= 0 ; i--) {                       // rank
                for (int j = 0; j < Position.BOARD_SIZE; j++) {                         // file
                    // Oracle: https://docs.oracle.com/javase/8/docs/api/javax/swing/JPanel.html
                    // Oracle: https://docs.oracle.com/javase/8/docs/api/java/awt/Color.html
                    // Add 1 as an offset due to how the panels are being rendered on the grid
                    PiecePanel currentPanel = new PiecePanel(java.awt.Color.decode(this.colors[(i + j) % 2]), java.awt.Color.decode(this.highlightColors[(i + j + 1) % 2]));
                    boardPanels[j][i] = currentPanel;
                    this.add(currentPanel);
                }
            }
        } else {
            for (int i = 0; i < Position.BOARD_SIZE; i++) {                             // rank
                for (int j = Position.BOARD_SIZE - 1; j >= 0; j--) {                    // file
                    // Oracle: https://docs.oracle.com/javase/8/docs/api/javax/swing/JPanel.html
                    // Oracle: https://docs.oracle.com/javase/8/docs/api/java/awt/Color.html
                    // Add 1 as an offset due to how the panels are being rendered on the grid
                    PiecePanel currentPanel = new PiecePanel(java.awt.Color.decode(this.colors[(i + j) % 2]), java.awt.Color.decode(this.highlightColors[(i + j + 1) % 2]));
                    boardPanels[j][i] = currentPanel;
                    this.add(currentPanel);
                }
            }
        }
    }

    public void switchColor(int file, int rank) {
        if (this.boardPanels[file][rank] instanceof PiecePanel) {
            PiecePanel piece = (PiecePanel) this.boardPanels[file][rank];
            piece.switchColor();
        }
    }

    public boolean isHighlighted(int file, int rank) {
        if (this.boardPanels[file][rank] instanceof PiecePanel) {
            PiecePanel piece = (PiecePanel) this.boardPanels[file][rank];
            return piece.isHighlighted;
        } else {
            return false;
        }
    }

    private class PiecePanel extends JPanel {
        public boolean isHighlighted;
        public java.awt.Color color;
        public java.awt.Color highlightColor;

        public PiecePanel(java.awt.Color color, java.awt.Color highlightColor) {
            this.color = color;
            this.highlightColor = highlightColor;
            this.setBackground(color);
        }

        public void switchColor() {
            if (!this.isHighlighted) {
                this.setBackground(this.highlightColor);
                this.isHighlighted = true;
            } else {
                setBackground(this.color);
                this.isHighlighted = false;
            }
        }
    }
}
package edu.sfsu.csc413.chess.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The pawn — the piece that breaks every rule the others follow.
 *
 * <p>It is the only piece that moves in just one direction, the only one whose
 * capture differs from its move, the only one with a special first move, and
 * the only one that turns into something else. It is worth noticing that all of
 * that awkwardness is contained in this one file. No other class in the engine
 * knows that pawns are strange. That containment is the payoff of polymorphism:
 * the irregular case costs one class, not a special case in every method that
 * touches a piece.
 *
 * <p>En passant is not handled here. Like castling, it depends on the previous
 * move rather than on the current board, so it waits for Week 15 when
 * {@code Game} owns the move history.
 */
public class Pawn extends Piece {

    /**
     * What a pawn may become on reaching the far rank.
     */
    private static final PieceType[] PROMOTION_CHOICES = { PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT };
    private boolean hasMoved = false;

    public Pawn(Color color) {
        super(color, PieceType.PAWN);
    }

    @Override
    public List<Move> pseudoLegalMoves(Board board, Position from) {
        ArrayList<Move> moves = new ArrayList<>();
        try {
            Position to = new Position(from.file(), from.rank() + this.color().pawnDirection());
            if (board.pieceAt(to) == null) {
                if (to.rank() == this.color().promotionRank()) {
                    Pawn.addPromotionMoves(moves, board, from, to);
                } else {
                    moves.add(Move.quiet(from, to, board.pieceAt(from)));
                }
            }

            // Only moves the pawn twice if it's on it's home rank
            if (from.rank() == this.color().pawnStartRank()) {
                Position second = new Position(from.file(), from.rank() + this.color().pawnDirection() * 2);
                if (board.pieceAt(to) == null && board.pieceAt(second) == null) {
                    // Technically possible to promote under certain chess variations by moving 2 squares, albeit unlikely
                    if (second.rank() == this.color().promotionRank()) {
                        Pawn.addPromotionMoves(moves, board, from, second);
                    } else {
                        moves.add(Move.quiet(from, second, board.pieceAt(from)));
                    }
                }
            }
        // Prevents the new move from being off the board without crashing the program
        } catch (IllegalArgumentException e) {}

        // Attack to the left, relative to white
        try {
            Position leftAttack = new Position(from.file() - 1, from.rank() + this.color().pawnDirection());
            if (board.pieceAt(leftAttack) != null && board.pieceAt(leftAttack).color() != this.color()) {
                if (leftAttack.rank() == this.color().pawnDirection()) {
                    Pawn.addPromotionMoves(moves, board, from, leftAttack);
                } else {
                    moves.add(Move.capture(from, leftAttack, board.pieceAt(from), board.pieceAt(leftAttack)));
                }
            }
        } catch (IllegalArgumentException e) {}

        // Attack to the right, relative to white
        try {
            Position rightAttack = new Position(from.file() + 1, this.color().pawnDirection());
            if (board.pieceAt(rightAttack) != null && board.pieceAt(rightAttack).color() != this.color()) {
                if (rightAttack.rank() == this.color().pawnDirection()) {
                    Pawn.addPromotionMoves(moves, board, from, rightAttack);
                } else {
                    moves.add(Move.capture(from, rightAttack, board.pieceAt(from), board.pieceAt(rightAttack)));
                }
            }
        } catch (IllegalArgumentException e) {}

        return moves;
    }

    /**
     * Helper method that adds in all the promotion moves to a List<Moves>
     */
    private static void addPromotionMoves(List<Move> moves, Board board, Position from, Position to) {
        for (PieceType promotesTo : Pawn.PROMOTION_CHOICES) {
            moves.add(Move.promotion(from, to, board.pieceAt(from), board.pieceAt(to), promotesTo));
        }
    }

    /**
     * A pawn attacks the two squares diagonally ahead of it, whether or not
     * anything stands there.
     *
     * <p>This override exists because the inherited version answers "can this
     * piece move to that square", and for a pawn that is the wrong question.
     * An empty square in front of a pawn is a square the pawn can move to but
     * does <em>not</em> attack — which matters enormously for king safety: a
     * king may not be blocked from a square merely because a pawn could advance
     * onto it, but it certainly may not step onto a square a pawn guards.
     */
    @Override
    public boolean attacks(Board board, Position from, Position target) {
        try {
            Position leftAttack = new Position(from.file() - 1, from.rank() + this.color().pawnDirection());
            if (leftAttack.equals(target)) {
                return true;
            }
        // Okay error to have; simply means the pawn is on the side of the board
        } catch (IllegalArgumentException e) {}

        try {
            Position rightAttack = new Position(from.file() + 1, from.rank() + this.color().pawnDirection());
            if (rightAttack.equals(target)) {
                return true;
            }
        // Okay error to have; simply means the pawn is on the side of the board
        } catch (IllegalArgumentException e) {}

        return false;
    }
}

package edu.sfsu.csc413.chess.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Tests for the two sides and the pawn facts they carry. */
class ColorTest {

    @Test
    @DisplayName("opposite flips the side, both ways")
    void oppositeFlips() {
        assertEquals(Color.BLACK, Color.WHITE.opposite());
        assertEquals(Color.WHITE, Color.BLACK.opposite());
    }

    @Test
    @DisplayName("pawns advance in opposite directions")
    void pawnDirections() {
        assertEquals(1, Color.WHITE.pawnDirection());
        assertEquals(-1, Color.BLACK.pawnDirection());
    }

    @Test
    @DisplayName("pawn start and promotion ranks are 0-based")
    void pawnRanks() {
        assertEquals(1, Color.WHITE.pawnStartRank());
        assertEquals(6, Color.BLACK.pawnStartRank());
        assertEquals(7, Color.WHITE.promotionRank());
        assertEquals(0, Color.BLACK.promotionRank());
    }
}

package edu.sfsu.csc413.chess.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Tests for coordinate handling and algebraic notation. */
class PositionTest {

    @Test
    @DisplayName("parses algebraic notation into 0-based coordinates")
    void parsesAlgebraic() {
        Position e2 = Position.parse("e2");
        assertEquals(4, e2.file());
        assertEquals(1, e2.rank());
    }

    @Test
    @DisplayName("round-trips through algebraic notation")
    void roundTrips() {
        assertEquals("a1", Position.parse("a1").toString());
        assertEquals("h8", Position.parse("h8").toString());
        assertEquals("e4", new Position(4, 3).toString());
    }

    @Test
    @DisplayName("two positions with the same coordinates are equal")
    void valueEquality() {
        assertEquals(new Position(4, 3), Position.parse("e4"));
    }

    @Test
    @DisplayName("rejects coordinates off the board")
    void rejectsOffBoard() {
        assertThrows(IllegalArgumentException.class, () -> new Position(8, 0));
        assertThrows(IllegalArgumentException.class, () -> new Position(0, -1));
        assertThrows(IllegalArgumentException.class, () -> Position.parse("j9"));
    }

    @Test
    @DisplayName("offset returns null rather than throwing when it leaves the board")
    void offsetOffBoardIsNull() {
        assertNull(Position.parse("a1").offsetOrNull(-1, 0));
        assertEquals(Position.parse("b2"), Position.parse("a1").offsetOrNull(1, 1));
    }
}

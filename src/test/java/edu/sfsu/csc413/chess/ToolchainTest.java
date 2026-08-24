package edu.sfsu.csc413.chess;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Proves the toolchain works before any chess exists.
 *
 * <p>If these three pass, your JDK, Maven, and JUnit are all wired up correctly
 * and you can start M1. They are deleted once the real tests arrive.
 */
class ToolchainTest {

    @Test
    @DisplayName("JUnit runs at all")
    void junitRuns() {
        assertEquals(4, 2 + 2);
    }

    @Test
    @DisplayName("running on JDK 25 or newer")
    void jdkIsNewEnough() {
        assertTrue(Runtime.version().feature() >= 25,
                "Need JDK 25+. See guides/environment-setup.md");
    }

    @Test
    @DisplayName("the main class is on the classpath")
    void mainClassExists() {
        assertEquals("Main", Main.class.getSimpleName());
    }
}

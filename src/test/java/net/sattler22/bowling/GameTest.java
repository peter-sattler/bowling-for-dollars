package net.sattler22.bowling;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Ten Pin Bowling Game Test Harness
 *
 * @author Pete Sattler
 * @version August 2025
 */
final class GameTest {

    private static final Logger logger = LoggerFactory.getLogger(GameTest.class);

    @Test
    void testIsOpen() {
        final Game peteGame = new Game("Pete");

        final int firstRoll = 8;
        final int secondRoll = 1;
        peteGame.roll(firstRoll);
        peteGame.roll(secondRoll);
        assertEquals(firstRoll + secondRoll, peteGame.score());

        peteGame.strike();

        peteGame.roll(4);
        peteGame.roll(2);

        logger.info("{}'s score after {} frame{}: {}",
                peteGame.playerName(), peteGame.size(), peteGame.size() == 1 ? "" : "s", peteGame.score());
    }
}

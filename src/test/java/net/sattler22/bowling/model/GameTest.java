package net.sattler22.bowling.model;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ten Pin Bowling Game Test Harness
 *
 * @author Pete Sattler
 * @version October 2025
 */
final class GameTest {

    private static final String BLANKS = "   ";

    @Test
    void playerName_whenNull_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Game(null)
        );
    }

    @Test
    void playerName_whenEmpty_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Game("")
        );
    }

    @Test
    void playerName_whenBlank_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Game(BLANKS)
        );
    }

    @Test
    void playerName_newInstance_thenSuccessful() {
        final String playerName = "Bob Wire";
        final Game game = new Game(playerName);
        assertEquals(playerName, game.playerName());
    }

    @Test
    void isOver_withNoFrames_thenReturnFalse() {
        assertFalse(new Game("Sandy Banks").isOver());
    }

    @Test
    void isOver_withOneFrame_thenReturnFalse() {
        final Game game = new Game("Barb Wire");
        game.addFrame(DefaultFrame.strike());
        assertFalse(game.isOver());
    }

    @Test
    void isOver_withMaxFrames_thenReturnTrue() {
        final Game game = new Game("Eileen Dover");
        IntStream.range(0, Game.MAX_FRAMES - 1)
                .forEach(nbrPins -> game.addFrame(new DefaultFrame(nbrPins, 0)));
        game.addFrame(new FinalFrame(9, 0, 0));
        assertTrue(game.isOver());
    }
}

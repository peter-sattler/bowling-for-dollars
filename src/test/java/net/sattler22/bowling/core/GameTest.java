package net.sattler22.bowling.core;

import net.sattler22.bowling.model.DefaultFrame;
import net.sattler22.bowling.model.FinalFrame;
import net.sattler22.bowling.model.Frame;
import org.junit.jupiter.api.Test;

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
    void newInstance_withNullPlayerName_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Game(null)
        );
    }

    @Test
    void newInstance_withEmptyPlayerName_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Game("")
        );
    }

    @Test
    void newInstance_withBlankPlayerName_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Game(BLANKS)
        );
    }

    @Test
    void newInstance_withHappyPath_thenSuccessful() {
        final String playerName = "Pete Moss";
        final Game game = new Game(playerName);
        assertEquals(playerName, game.playerName());
    }

    @Test
    void isOver_withNoFrames_thenReturnFalse() {
        assertFalse(new Game("Sandy Banks").isOver());
    }

    @Test
    void isOver_withMaxFrames_thenReturnTrue() {
        assertTrue(zeroGame("Eileen Dover").isOver());
    }

    @Test
    void isPerfect_withTooFewPins_thenReturnFalse() {
        final Game game = zeroGame("Paige Turner");
        game.updateScore();
        assertFalse(game.isPerfect());
    }

    @Test
    void isPerfect_withHappyPath_thenReturnTrue() {
        final Game game = new Game("Burt Rentals");
        for (int i = 0; i < Game.MAX_FRAMES - 1; i++)
            game.addFrame(DefaultFrame.strike());
        game.addFrame(new FinalFrame(Frame.MAX_PINS, Frame.MAX_PINS, Frame.MAX_PINS));
        game.updateScore();
        assertTrue(game.isPerfect());
    }

    @Test
    void addFrame_withCompleteGame_thenThrowIllegalStateException() {
        final Game game = zeroGame("Gene Poole");
        final DefaultFrame extraRoll = DefaultFrame.strike();
        assertThrows(IllegalStateException.class, () ->
            game.addFrame(extraRoll)
        );
    }

    @Test
    void addFrame_withNullFrame_thenThrowIllegalArgumentException() {
        final Game game = new Game("Chris Cross");
        assertThrows(IllegalArgumentException.class, () ->
                game.addFrame(null)
        );
    }

    @Test
    void addFrame_withBadFinalFrame_thenThrowIllegalArgumentException() {
        final Game game = new Game("Joe Kerr");
        for (int i = 0; i < Game.MAX_FRAMES - 1; i++)
            game.addFrame(DefaultFrame.strike());
        final DefaultFrame evilFinalFrame = DefaultFrame.strike();
        assertThrows(IllegalArgumentException.class, () ->
                game.addFrame(evilFinalFrame)
        );
    }

    private static Game zeroGame(String playerName) {
        final Game game = new Game(playerName);
        for (int i = 0; i < Game.MAX_FRAMES - 1; i++)
            game.addFrame(new DefaultFrame(0, 0));
        game.addFrame(new FinalFrame(0, 0));
        return game;
    }
}

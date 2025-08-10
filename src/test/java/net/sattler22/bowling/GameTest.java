package net.sattler22.bowling;

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
 * @version August 2025
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
    void isOver_whenNoFrames_thenReturnFalse() {
        assertFalse(new Game("Sandy Banks").isOver());
    }

    @Test
    void isOver_whenOneFrame_thenReturnFalse() {
        final Game game = new Game("Barb Wire");
        game.strike();
        assertFalse(game.isOver());
    }

    @Test
    void isOver_whenMaxFrames_thenReturnTrue() {
        final Game game = new Game("Eileen Dover");
        IntStream.range(0, FrameList.MAX_FRAMES - 1).forEach(i ->
                game.strike()  //Default frames
        );
        game.roll(1);  //Final frame
        game.roll(1);  //Final frame
        assertTrue(game.isOver());
    }

    @Test
    void roll_whenTooManyFrames_thenThrowIllegalStateException() {
        final Game game = new Game("Burt Rentals");
        IntStream.range(0, FrameList.MAX_FRAMES - 1).forEach(i ->
                game.strike()  //Default frames
        );
        game.roll(1);  //Final frame
        game.roll(1);  //Final frame
        assertThrows(IllegalStateException.class, game::strike);
    }

    @Test
    void roll_whenFirstFrameIsOpen_thenVerifyFirstFrameScore() {
        final Game game = new Game("Mae Day");
        final int roll1 = 8;
        final int roll2 = 1;
        game.roll(roll1);
        game.roll(roll2);
        assertEquals(roll1 + roll2, game.score());
    }

    @Test
    void roll_whenFirstStrike_thenDoNotScoreFrame() {
        final Game game = new Game("Rob U. Blind");
        game.strike();
        assertEquals(0, game.score());
    }

    @Test
    void roll_whenFirstStrikePlusSingleRoll_thenDoNotScoreFrame() {
        final Game game = new Game("Story Teller");
        game.strike();
        game.roll(4);
        assertEquals(0, game.score());
    }

    @Test
    void roll_whenFirstStrikeThenOpenFrame_thenVerifyFirstFrameScore() {
        final Game game = new Game("Noah Lott");
        final int roll2 = 8;
        final int roll3 = 1;
        final int expectedBonus = roll2 + roll3;
        game.strike();
        game.roll(roll2);
        game.roll(roll3);
        assertEquals(Frame.MAX_PINS  + expectedBonus, game.frames().getFirst().score().orElse(0));
    }

    @Test
    void roll_whenFirstFrameOpenThenStrike_thenVerifyFirstFrameScore() {
        final Game game = new Game("Cliff Hanger");
        final int roll1 = 7;
        final int roll2 = 2;
        game.roll(roll1);
        game.roll(roll2);
        game.strike();
        assertEquals(roll1 + roll2, game.frames().getFirst().score().orElse(0));
    }

    @Test
    void roll_whenFirstFrameOpenThenStrike_thenVerifyAllFramesScore() {
        final Game game = new Game("Justin Time");
        final int roll1 = 7;
        final int roll2 = 2;
        final int roll4 = 4;
        final int roll5 = 2;
        game.roll(roll1);
        game.roll(roll2);
        game.strike();
        game.roll(roll4);
        game.roll(roll5);
        assertEquals(roll1 + roll2, game.frames().getFirst().score().orElse(0));
        assertEquals(25, game.frames().get(1).score().orElse(0));
        assertEquals(31, game.frames().get(2).score().orElse(0));
    }

    @Test
    void roll_whenThreeConsecutiveStrikes_thenVerifyFirstFrameScore() {
        final Game game = new Game("Gene Pool");
        final int expectedScore = Frame.MAX_PINS;
        final int expectedBonus = Frame.MAX_PINS;
        game.strike();
        game.strike();
        game.strike();
        assertEquals(expectedScore + expectedBonus, game.frames().getFirst().score().orElse(0));
    }
}

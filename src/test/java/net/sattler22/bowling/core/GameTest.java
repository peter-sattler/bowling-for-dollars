package net.sattler22.bowling.core;

import net.sattler22.bowling.model.DefaultFrame;
import net.sattler22.bowling.model.FinalFrame;
import net.sattler22.bowling.model.Frame;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        final Game game = new Game("Justin Sane");
        for (int i = 0; i < Game.MAX_FRAMES - 1; i++)
            game.addFrame(DefaultFrame.strike());
        final DefaultFrame evilFinalFrame = DefaultFrame.strike();
        assertThrows(IllegalArgumentException.class, () ->
                game.addFrame(evilFinalFrame)
        );
    }

    @Test
    void updateScore_withZeroFrame_thenNoBonus() {
        final Game game = new Game("Mo Lasses");
        game.addFrame(new DefaultFrame(0, 0));
        final List<Frame> updatedFrames = game.updateScore();
        assertEquals(1, updatedFrames.size());
        assertExpectedScore(0, updatedFrames, 0);
    }

    @Test
    void updateScore_withOpenFrame_thenNoBonus() {
        final Game game = new Game("Joe Kerr");
        final int nbrPins1 = 3;
        final int nbrPins2 = 6;
        game.addFrame(new DefaultFrame(nbrPins1, nbrPins2));
        final List<Frame> updatedFrames = game.updateScore();
        final int expectedFrame1 = nbrPins1 + nbrPins2;  //Frame #1: 3 + 6 = 9 (9)
        assertEquals(1, updatedFrames.size());
        assertExpectedScore(expectedFrame1, updatedFrames, 0);
    }

    @Test
    void updateScore_withSpare_thenReturnNoUpdatedFrames() {
        final Game game = new Game("Cat Fish");
        game.addFrame(new DefaultFrame(5, 5));
        assertEquals(0, game.updateScore().size());
    }

    @Test
    void updateScore_withSpare_thenNextRollBonus() {
        final Game game = new Game("Bark Twain");
        final int nbrPins1 = 2;
        final int nbrPins2 = 8;
        final int nbrPins3 = 7;
        final int nbrPins4 = 1;
        game.addFrame(new DefaultFrame(nbrPins1, nbrPins2));
        game.addFrame(new DefaultFrame(nbrPins3, nbrPins4));
        final List<Frame> updatedFrames = game.updateScore();
        final int expectedFrame1 = nbrPins1 + nbrPins2 + nbrPins3;  //Frame #1: / + 7 = 17 (17)
        final int expectedFrame2 = nbrPins3 + nbrPins4;             //Frame #2: 7 + 1 = 8 (25)
        assertEquals(2, updatedFrames.size());
        assertExpectedScore(expectedFrame1, updatedFrames, 0);
        assertExpectedScore(expectedFrame1 + expectedFrame2, updatedFrames, 1);
    }

    @Test
    void updateScore_withOneStrike_thenReturnNoUpdatedFrames() {
        final Game game = new Game("Frank Lee Speaking");
        game.addFrame(DefaultFrame.strike());
        assertEquals(0, game.updateScore().size());
    }

    @Test
    void updateScore_withTwoStrikes_thenReturnNoUpdatedFrames() {
        final Game game = new Game("Bill Board");
        game.addFrame( DefaultFrame.strike());
        game.addFrame( DefaultFrame.strike());
        assertEquals(0, game.updateScore().size());
    }

    @Test
    void updateScore_withStrike_thenNextTwoRollsOneFrameBonusOpen() {
        final Game game = new Game("Terry Aki");
        final int nbrPins1 = Frame.MAX_PINS;
        final int nbrPins2 = 1;
        final int nbrPins3 = 3;
        game.addFrame(new DefaultFrame(nbrPins1, 0));
        game.addFrame(new DefaultFrame(nbrPins2, nbrPins3));
        final List<Frame> updatedFrames = game.updateScore();
        final int expectedFrame1 = nbrPins1 + nbrPins2 + nbrPins3;  //Frame #1: X + 1 + 3 = 14 (14)
        final int expectedFrame2 = nbrPins2 + nbrPins3;             //Frame #2: 1 + 3 = 4 (18)
        assertEquals(2, updatedFrames.size());
        assertExpectedScore(expectedFrame1, updatedFrames, 0);
        assertExpectedScore(expectedFrame1 + expectedFrame2, updatedFrames, 1);
    }

    @Test
    void updateScore_withStrike_thenNextTwoRollsOneFrameBonusSpare() {
        final Game game = new Game("Justin Time");
        final int nbrPins1 = Frame.MAX_PINS;
        final int nbrPins2 = 6;
        final int nbrPins3 = 4;
        final int nbrPins4 = 2;
        final int nbrPins5 = 0;
        game.addFrame(new DefaultFrame(nbrPins1, 0));
        game.addFrame(new DefaultFrame(nbrPins2, nbrPins3));
        game.addFrame(new DefaultFrame(nbrPins4, 0));
        final List<Frame> updatedFrames = game.updateScore();
        final int expectedFrame1 = nbrPins1 + nbrPins2 + nbrPins3;  //Frame #1: X + / = 20 (20)
        final int expectedFrame2 = nbrPins2 + nbrPins3 + nbrPins4;  //Frame #2: / + 2 = 12 (32)
        final int expectedFrame3 = nbrPins4 + nbrPins5;             //Frame #3: 2 + 0 = 2 (34)
        assertEquals(3, updatedFrames.size());
        assertExpectedScore(expectedFrame1, updatedFrames, 0);
        assertExpectedScore(expectedFrame1 + expectedFrame2, updatedFrames, 1);
        assertExpectedScore(expectedFrame1 + expectedFrame2 + expectedFrame3, updatedFrames, 2);
    }

    @Test
    void updateScore_withStrike_thenNextTwoRollsOneFrameBonusFinalFrame() {
        final Game game = new Game("Howl E. Coyote");
        final int nbrInitialFrames = Game.MAX_FRAMES - 2;
        final int nbrPins1Thru8FirstAttempt = 1;
        final int nbrPins1Thru8SecondAttempt = 2;
        final int nbrPins9FirstAttempt = Frame.MAX_PINS;
        final int nbrPins10FirstAttempt = 6;
        final int nbrPins10SecondAttempt = 1;
        for (int i = 0; i < nbrInitialFrames; i++)
            game.addFrame(new DefaultFrame(nbrPins1Thru8FirstAttempt, nbrPins1Thru8SecondAttempt));
        game.addFrame(new DefaultFrame(nbrPins9FirstAttempt, 0));
        game.addFrame(new FinalFrame(nbrPins10FirstAttempt, nbrPins10SecondAttempt));
        final List<Frame> updatedFrames = game.updateScore();
        final int expectedFrame1Thru8 =
                (nbrPins1Thru8FirstAttempt + nbrPins1Thru8SecondAttempt) * nbrInitialFrames;  //Frame #1-8: 3 * 8 = 24 (24)
        final int expectedFrame9 =
                nbrPins9FirstAttempt + nbrPins10FirstAttempt + nbrPins10SecondAttempt;        //Frame #9: X + 6 + 1 = 17 (41)
        final int expectedFrame10 = nbrPins10FirstAttempt + nbrPins10SecondAttempt;           //Frame #10: 6 + 1 = 7 (48)
        assertEquals(Game.MAX_FRAMES, updatedFrames.size());
        assertExpectedScore(expectedFrame1Thru8, updatedFrames, 7);
        assertExpectedScore(expectedFrame1Thru8 + expectedFrame9, updatedFrames, 8);
        assertExpectedScore(expectedFrame1Thru8 + expectedFrame9 + expectedFrame10, updatedFrames, 9);
    }

    @Test
    void updateScore_withStrike_thenNextTwoRollsTwoFramesBonus() {
        final Game game = new Game("Anita Bath");
        final int nbrInitialFrames = Game.MAX_FRAMES - 3;
        final int nbrPins1Thru7FirstAttempt = 4;
        final int nbrPins1Thru7SecondAttempt = 5;
        final int nbrPins8FirstAttempt = Frame.MAX_PINS;
        final int nbrPins9FirstAttempt = Frame.MAX_PINS;
        final int nbrPins10FirstAttempt = 8;
        final int nbrPins10SecondAttempt = 1;
        for (int i = 0; i < nbrInitialFrames; i++)
            game.addFrame(new DefaultFrame(nbrPins1Thru7FirstAttempt, nbrPins1Thru7SecondAttempt));
        game.addFrame(new DefaultFrame(nbrPins8FirstAttempt, 0));
        game.addFrame(new DefaultFrame(nbrPins9FirstAttempt, 0));
        game.addFrame(new FinalFrame(nbrPins10FirstAttempt, nbrPins10SecondAttempt));
        final List<Frame> updatedFrames = game.updateScore();
        final int expectedFrame1Thru7 =
                (nbrPins1Thru7FirstAttempt + nbrPins1Thru7SecondAttempt) * nbrInitialFrames;  //Frame #1-7: 9 * 7 = 63 (63)
        final int expectedFrame8 =
                nbrPins8FirstAttempt + nbrPins9FirstAttempt + nbrPins10FirstAttempt;          //Frame #8: X + X + 8 = 28 (91)
        final int expectedFrame9 =
                nbrPins9FirstAttempt + nbrPins10FirstAttempt + nbrPins10SecondAttempt;        //Frame #9: X + 8 + 1 = 19 (110)
        final int expectedFrame10 = nbrPins10FirstAttempt + nbrPins10SecondAttempt;           //Frame #10: 8 + 1 = 9 (118)
        assertEquals(Game.MAX_FRAMES, updatedFrames.size());
        assertExpectedScore(expectedFrame1Thru7, updatedFrames, 6);
        assertExpectedScore(expectedFrame1Thru7 + expectedFrame8, updatedFrames, 7);
        assertExpectedScore(expectedFrame1Thru7 + expectedFrame8 + expectedFrame9, updatedFrames, 8);
        assertExpectedScore(expectedFrame1Thru7 + expectedFrame8 + expectedFrame9 + expectedFrame10, updatedFrames, 9);
    }

    @Test
    void score_withNoFrames_thenReturnZeroScore() {
        final Game game = new Game("Ben O’Drill");
        assertEquals(0, game.score());
    }

    @Test
    void score_withOneFrameWithNoUpdate_thenReturnZeroScore() {
        final Game game = new Game("Chris P. Bacon");
        game.addFrame(new DefaultFrame(3, 2));
        assertEquals(0, game.score());
    }

    @Test
    void score_withOneFrameWithUpdate_thenReturnCorrectScore() {
        final Game game = new Game("Mary Christmas");
        game.addFrame(new DefaultFrame(1, 0));
        game.updateScore();
        assertEquals(1, game.score());
    }

    private static Game zeroGame(String playerName) {
        final Game game = new Game(playerName);
        for (int i = 0; i < Game.MAX_FRAMES - 1; i++)
            game.addFrame(new DefaultFrame(0, 0));
        game.addFrame(new FinalFrame(0, 0));
        return game;
    }

    private static void assertExpectedScore(int expectedScore, List<Frame> frames, int index) {
        assertEquals(expectedScore, frames.get(index).score().orElse(-1));
    }
}

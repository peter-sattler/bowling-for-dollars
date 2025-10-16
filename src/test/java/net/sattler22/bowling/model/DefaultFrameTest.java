package net.sattler22.bowling.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ten Pin Bowling Default Frame Test Harness
 *
 * @author Pete Sattler
 * @since July 2025
 * @version October 2025
 */
final class DefaultFrameTest {

    @Test
    void newInstance_withTooFewPins1_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new DefaultFrame(-1, 2)
        );
    }

    @Test
    void newInstance_withTooFewPins2_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new DefaultFrame(1, -1)
        );
    }

    @Test
    void newInstance_withTooManyPins1_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new DefaultFrame(Frame.MAX_PINS + 1, 2)
        );
    }

    @Test
    void newInstance_withTooManyPins2_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new DefaultFrame(1, Frame.MAX_PINS + 1)
        );
    }

    @Test
    void newInstance_withTooManyTotalPins_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new DefaultFrame(5, 6)
        );
    }

    @Test
    void newInstance_withHappyPath_thenSuccessful() {
        final int nbrPins1 = 3;
        final int nbrPins2 = 4;
        final DefaultFrame defaultFrame = new DefaultFrame(nbrPins1, nbrPins2);
        assertEquals(nbrPins1, defaultFrame.firstRoll());
        assertEquals(nbrPins2, defaultFrame.secondRoll());
    }

    @Test
    void strike_withHappyPath_thenSuccessful() {
        final DefaultFrame strikeFrame = DefaultFrame.strike();
        assertEquals(Frame.MAX_PINS, strikeFrame.firstRoll());
        assertEquals(0, strikeFrame.secondRoll());
    }

    @Test
    void isZero_withTooManyPins_thenReturnFalse() {
        assertFalse(new DefaultFrame(1, 0).isZero());
    }

    @Test
    void isZero_withHappyPath_thenReturnTrue() {
        assertTrue(zeroFrame().isZero());
    }

    @Test
    void isOpen_withZeroPins_thenReturnTrue() {
        assertTrue(zeroFrame().isOpen());
    }

    @Test
    void isOpen_withHappyPath_thenReturnTrue() {
        assertTrue(new DefaultFrame(1, 6).isOpen());
    }

    @Test
    void isOpen_withSpare_thenReturnFalse() {
        assertFalse(spareFrame(1).isOpen());
    }

    @Test
    void isOpen_withStrike_thenReturnFalse() {
        assertFalse(DefaultFrame.strike().isOpen());
    }

    @Test
    void isSpare_withZeroPins_thenReturnFalse() {
        assertFalse(zeroFrame().isSpare());
    }

    @Test
    void isSpare_withOpenFrame_thenReturnFalse() {
        assertFalse(new DefaultFrame(2, 6).isSpare());
    }

    @Test
    void isSpare_withHappyPath_thenReturnTrue() {
        assertTrue(spareFrame(5).isSpare());
    }

    @Test
    void isSpare_withStrike_thenReturnFalse() {
        assertFalse(DefaultFrame.strike().isSpare());
    }

    @Test
    void isStrike_withZeroPins_thenReturnFalse() {
        assertFalse(zeroFrame().isStrike());
    }

    @Test
    void isStrike_withOpenFrame_thenReturnFalse() {
        assertFalse(new DefaultFrame(4, 4).isStrike());
    }

    @Test
    void isStrike_withSpare_thenReturnFalse() {
        assertFalse(spareFrame(9).isStrike());
    }

    @Test
    void isStrike_withHappyPath_thenReturnTrue() {
        assertTrue(DefaultFrame.strike().isStrike());
    }

    @Test
    void hasScore_withoutUpdate_thenReturnFalse() {
        assertFalse(zeroFrame().hasScore());
    }

    @Test
    void hasScore_withHappyPath_thenReturnTrue() {
        final DefaultFrame zeroFrame = zeroFrame();
        zeroFrame.updateScore(0, 0);
        assertTrue(zeroFrame.hasScore());
    }

    @Test
    void score_withoutUpdate_thenReturnNegativeOne() {
        assertEquals(-1, zeroFrame().score());
    }

    @Test
    void score_withHappyPath_thenReturnTrue() {
        final DefaultFrame zeroFrame = zeroFrame();
        zeroFrame.updateScore(0, 0);
        assertEquals(0, zeroFrame.score());
    }

    @Test
    void updateScore_withNegativeStartingPoints_thenThrowIllegalArgumentException() {
        final DefaultFrame zeroFrame = zeroFrame();
        assertThrows(IllegalArgumentException.class, () ->
                zeroFrame.updateScore(-1, 0));
    }

    @Test
    void updateScore_withNegativeBonusPoints_thenThrowIllegalArgumentException() {
        final DefaultFrame zeroFrame = zeroFrame();
        assertThrows(IllegalArgumentException.class, () ->
                zeroFrame.updateScore(0,-1));
    }

   @Test
   void updateScore_withHappyPath_thenSuccessful() {
        final int nbrPins1 = 6;
        final int nbrPins2 = 3;
        final int start = 25;
        final int bonus = 10;
        final DefaultFrame openFrame = new DefaultFrame(nbrPins1, nbrPins2);
        openFrame.updateScore(start, bonus);
        assertEquals(nbrPins1 + nbrPins2 + start + bonus, openFrame.score());
   }

    @Test
    void firstRoll_withHappyPath_thenSuccessful() {
        final int nbrPins1 = 7;
        final DefaultFrame defaultFrame = new DefaultFrame(nbrPins1, 0);
        assertEquals(nbrPins1, defaultFrame.firstRoll());
    }

    @Test
    void secondRoll_withHappyPath_thenSuccessful() {
        final int nbrPins2 = 2;
        final DefaultFrame defaultFrame = new DefaultFrame(0, nbrPins2);
        assertEquals(nbrPins2, defaultFrame.secondRoll());
    }

    @Test
    void total_withHappyPath_thenSuccessful() {
        final int nbrPins1 = 7;
        final int nbrPins2 = 2;
        final DefaultFrame defaultFrame = new DefaultFrame(nbrPins1, nbrPins2);
        assertEquals(nbrPins1 + nbrPins2, defaultFrame.total());
    }

    @Test
    void equals_whenSame_thenReturnTrue() {
        final int nbrPins1 = 2;
        final int nbrPins2 = 0;
        final DefaultFrame openFrame1 = new DefaultFrame(nbrPins1, nbrPins2);
        final DefaultFrame openFrame2 = new DefaultFrame(nbrPins1, nbrPins2);
        assertEquals(openFrame1, openFrame2);
    }

    @Test
    void equals_whenDifferent_thenReturnFalse() {
        final int nbrPins1 = 1;
        final DefaultFrame openFrame1 = new DefaultFrame(nbrPins1, 1);
        final DefaultFrame openFrame2 = new DefaultFrame(nbrPins1, 2);
        assertNotEquals(openFrame1, openFrame2);
    }

    private static DefaultFrame zeroFrame() {
        return new DefaultFrame(0, 0);
    }

    private static DefaultFrame spareFrame(int nbrPins1) {
        return new DefaultFrame(nbrPins1, Frame.MAX_PINS - nbrPins1);
    }
}

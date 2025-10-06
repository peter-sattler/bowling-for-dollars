package net.sattler22.bowling;

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
    void recordStrike_thenFirstRollHasMaxPins() {
        final DefaultFrame strikeFrame = DefaultFrame.strike();
        assertEquals(Frame.MAX_PINS, strikeFrame.firstRoll());
        assertEquals(0, strikeFrame.secondRoll());
        assertTrue(strikeFrame.isStrike());
        assertFalse(strikeFrame.isSpare());
        assertFalse(strikeFrame.isOpen());
        assertFalse(strikeFrame.isZero());
        assertFalse(strikeFrame.hasScore());
        assertEquals(-1, strikeFrame.score());
        assertEquals(Frame.MAX_PINS, strikeFrame.total());
    }

    @Test
    void recordNonStrikeFrame_thenTotalHasLessThanMaxPins() {
        final int nbrPins1 = 2;
        final int nbrPins2 = 0;
        final int expectedTotal = nbrPins1 + nbrPins2;
        final DefaultFrame openFrame = new DefaultFrame(nbrPins1, nbrPins2);
        assertEquals(nbrPins1, openFrame.firstRoll());
        assertEquals(nbrPins2, openFrame.secondRoll());
        assertFalse(openFrame.isStrike());
        assertFalse(openFrame.isSpare());
        assertTrue(openFrame.isOpen());
        assertFalse(openFrame.isZero());
        assertFalse(openFrame.hasScore());
        assertEquals(-1, openFrame.score());
        assertEquals(expectedTotal, openFrame.total());
    }

    @Test
    void recordNonStrikeFrame_withMoreThanMaxPins_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new DefaultFrame(Frame.MAX_PINS, 1)
        );
    }

    @Test
    void recordNonStrikeFrame_withTooManyPins_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new DefaultFrame(Frame.MAX_PINS, Frame.MAX_PINS)
        );
    }

    @Test
    void recordNonStrikeFrame_withZeroPins_thenTotalHasNoPins() {
        final int nbrPins1 = 0;
        final int nbrPins2 = 0;
        final DefaultFrame zeroFrame = new DefaultFrame(0, 0);
        assertEquals(nbrPins1, zeroFrame.firstRoll());
        assertEquals(nbrPins2, zeroFrame.secondRoll());
        assertFalse(zeroFrame.isStrike());
        assertFalse(zeroFrame.isSpare());
        assertTrue(zeroFrame.isOpen());
        assertTrue(zeroFrame.isZero());
        assertFalse(zeroFrame.hasScore());
        assertEquals(-1, zeroFrame.score());
        assertEquals(nbrPins1 + nbrPins2, zeroFrame.total());
    }

    @Test
    void score_withoutUpdate_thenReturnEmpty() {
        final DefaultFrame strikeFrame = DefaultFrame.strike();
        assertFalse(strikeFrame.hasScore());
        assertEquals(-1, strikeFrame.score());
    }

    @Test
    void score_withNegativeStartingPoints_thenThrowIllegalArgumentException() {
        final DefaultFrame zeroFrame = new DefaultFrame(0, 0);
        assertThrows(IllegalArgumentException.class, () ->
                zeroFrame.updateScore(-1, 0));
    }

    @Test
    void score_withNoStartingPointsNoBonusUpdate_thenReturnScore() {
        final int nbrPins1 = 6;
        final int expectedScore = Frame.MAX_PINS;
        final DefaultFrame spareFrame = new DefaultFrame(nbrPins1, Frame.MAX_PINS - nbrPins1);
        spareFrame.updateScore(0, 0);
        assertTrue(spareFrame.hasScore());
        assertEquals(expectedScore, spareFrame.score());
    }

    @Test
    void score_withNoStartingPointsBonusUpdate_thenReturnScore() {
        final int nbrPins1 = 6;
        final int bonusPins = 3;
        final int expectedScore = Frame.MAX_PINS + bonusPins;
        final DefaultFrame spareFrame = new DefaultFrame(nbrPins1, Frame.MAX_PINS - nbrPins1);
        spareFrame.updateScore(0, bonusPins);
        assertTrue(spareFrame.hasScore());
        assertEquals(expectedScore, spareFrame.score());
    }

    @Test
    void score_withStartingPointsNoBonusUpdate_thenReturnScore() {
        final int startingPoints = 4;
        final int nbrPins1 = 6;
        final int expectedScore = startingPoints + Frame.MAX_PINS;
        final DefaultFrame spareFrame = new DefaultFrame(nbrPins1, Frame.MAX_PINS - nbrPins1);
        spareFrame.updateScore(startingPoints, 0);
        assertTrue(spareFrame.hasScore());
        assertEquals(expectedScore, spareFrame.score());
    }

    @Test
    void score_withStartingPointsBonusUpdate_thenReturnScore() {
        final int startingPoints = 5;
        final int nbrPins1 = 6;
        final int bonusPins = 3;
        final int expectedScore = startingPoints + Frame.MAX_PINS + bonusPins;
        final DefaultFrame spareFrame = new DefaultFrame(nbrPins1, Frame.MAX_PINS - nbrPins1);
        spareFrame.updateScore(startingPoints, bonusPins);
        assertTrue(spareFrame.hasScore());
        assertEquals(expectedScore, spareFrame.score());
    }

    @Test
    void score_withNegativeBonus_thenThrowIllegalArgumentException() {
        final DefaultFrame zeroFrame = new DefaultFrame(0, 0);
        assertThrows(IllegalArgumentException.class, () ->
                zeroFrame.updateScore(0,-1));
    }

    @Test
    void compare_equalFrames_thenReturnTrue() {
        final int nbrPins1 = 2;
        final int nbrPins2 = 0;
        final DefaultFrame openFrame1 = new DefaultFrame(nbrPins1, nbrPins2);
        final DefaultFrame openFrame2 = new DefaultFrame(nbrPins1, nbrPins2);
        assertEquals(openFrame1, openFrame2);
    }

    @Test
    void compare_unequalFrames_thenReturnFalse() {
        final int nbrPins1 = 1;
        final DefaultFrame openFrame1 = new DefaultFrame(nbrPins1, 1);
        final DefaultFrame openFrame2 = new DefaultFrame(nbrPins1, 2);
        assertNotEquals(openFrame1, openFrame2);
    }
}

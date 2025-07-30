package net.sattler22.bowling;

import org.junit.jupiter.api.Test;

import java.util.OptionalInt;

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
 * @version August 2025
 */
final class DefaultFrameTest {

    @Test
    void recordStrike_thenFirstAttemptHasMaxPins() {
        final DefaultFrame strikeFrame = DefaultFrame.strike();
        assertEquals(Frame.MAX_PINS, strikeFrame.firstAttempt());
        assertEquals(0, strikeFrame.secondAttempt());
        assertTrue(strikeFrame.isStrike());
        assertFalse(strikeFrame.isSpare());
        assertFalse(strikeFrame.isOpen());
        assertFalse(strikeFrame.isZero());
        assertFalse(strikeFrame.hasScore());
        assertTrue(strikeFrame.score().isEmpty());
        assertEquals(Frame.MAX_PINS, strikeFrame.total());
    }

    @Test
    void recordNonStrikeFrame_thenTotalHasLessThanMaxPins() {
        final int nbrPins1 = 2;
        final int nbrPins2 = 0;
        final int expectedTotal = nbrPins1 + nbrPins2;
        final DefaultFrame openFrame = DefaultFrame.nonStrike(nbrPins1, nbrPins2);
        assertEquals(nbrPins1, openFrame.firstAttempt());
        assertEquals(nbrPins2, openFrame.secondAttempt());
        assertFalse(openFrame.isStrike());
        assertFalse(openFrame.isSpare());
        assertTrue(openFrame.isOpen());
        assertFalse(openFrame.isZero());
        assertFalse(openFrame.hasScore());
        assertTrue(openFrame.score().isEmpty());
        assertEquals(expectedTotal, openFrame.total());
    }

    @Test
    void recordNonStrikeFrame_withMoreThanMaxPins_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                DefaultFrame.nonStrike(Frame.MAX_PINS, 1)
        );
    }

    @Test
    void recordNonStrikeFrame_withTooManyPins_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                DefaultFrame.nonStrike(Frame.MAX_PINS, 0)
        );
    }

    @Test
    void recordNonStrikeFrame_withZeroPins_thenTotalHasNoPins() {
        final int nbrPins1 = 0;
        final int nbrPins2 = 0;
        final DefaultFrame zeroFrame = DefaultFrame.nonStrike(0, 0);
        assertEquals(nbrPins1, zeroFrame.firstAttempt());
        assertEquals(nbrPins2, zeroFrame.secondAttempt());
        assertFalse(zeroFrame.isStrike());
        assertFalse(zeroFrame.isSpare());
        assertTrue(zeroFrame.isOpen());
        assertTrue(zeroFrame.isZero());
        assertFalse(zeroFrame.hasScore());
        assertTrue(zeroFrame.score().isEmpty());
        assertEquals(nbrPins1 + nbrPins2, zeroFrame.total());
    }

    @Test
    void score_withoutUpdate_thenReturnEmpty() {
        final DefaultFrame strikeFrame = DefaultFrame.strike();
        assertFalse(strikeFrame.hasScore());
        assertTrue(strikeFrame.score().isEmpty());
    }

    @Test
    void score_withNegativeStartingPoints_thenThrowIllegalArgumentException() {
        final DefaultFrame zeroFrame = DefaultFrame.nonStrike(0, 0);
        assertThrows(IllegalArgumentException.class, () ->
                zeroFrame.updateScore(-1, 0));
    }

    @Test
    void score_withStartingPointsTooLarge_thenThrowIllegalArgumentException() {
        final DefaultFrame zeroFrame = DefaultFrame.nonStrike(0, 0);
        assertThrows(IllegalArgumentException.class, () ->
                zeroFrame.updateScore(Frame.MAX_SCORE + 1, 0));
    }

    @Test
    void score_withNoStartingPointsNoBonusUpdate_thenReturnScore() {
        final int nbrPins1 = 6;
        final OptionalInt expectedScore = OptionalInt.of(Frame.MAX_PINS);
        final DefaultFrame spareFrame = DefaultFrame.nonStrike(nbrPins1, Frame.MAX_PINS - nbrPins1);
        spareFrame.updateScore(0, 0);
        assertTrue(spareFrame.hasScore());
        assertEquals(expectedScore, spareFrame.score());
    }

    @Test
    void score_withNoStartingPointsBonusUpdate_thenReturnScore() {
        final int nbrPins1 = 6;
        final int bonusPins = 3;
        final OptionalInt expectedScore = OptionalInt.of(Frame.MAX_PINS + bonusPins);
        final DefaultFrame spareFrame = DefaultFrame.nonStrike(nbrPins1, Frame.MAX_PINS - nbrPins1);
        spareFrame.updateScore(0, bonusPins);
        assertTrue(spareFrame.hasScore());
        assertEquals(expectedScore, spareFrame.score());
    }

    @Test
    void score_withStartingPointsNoBonusUpdate_thenReturnScore() {
        final int startingPoints = 4;
        final int nbrPins1 = 6;
        final OptionalInt expectedScore = OptionalInt.of(startingPoints + Frame.MAX_PINS);
        final DefaultFrame spareFrame = DefaultFrame.nonStrike(nbrPins1, Frame.MAX_PINS - nbrPins1);
        spareFrame.updateScore(startingPoints, 0);
        assertTrue(spareFrame.hasScore());
        assertEquals(expectedScore, spareFrame.score());
    }

    @Test
    void score_withStartingPointsBonusUpdate_thenReturnScore() {
        final int startingPoints = 5;
        final int nbrPins1 = 6;
        final int bonusPins = 3;
        final OptionalInt expectedScore = OptionalInt.of(startingPoints + Frame.MAX_PINS + bonusPins);
        final DefaultFrame spareFrame = DefaultFrame.nonStrike(nbrPins1, Frame.MAX_PINS - nbrPins1);
        spareFrame.updateScore(startingPoints, bonusPins);
        assertTrue(spareFrame.hasScore());
        assertEquals(expectedScore, spareFrame.score());
    }

    @Test
    void score_withNegativeBonus_thenThrowIllegalArgumentException() {
        final DefaultFrame zeroFrame = DefaultFrame.nonStrike(0, 0);
        assertThrows(IllegalArgumentException.class, () ->
                zeroFrame.updateScore(0,-1));
    }

    @Test
    void score_withBonusTooLarge_thenThrowIllegalArgumentException() {
        final DefaultFrame zeroFrame = DefaultFrame.nonStrike(0, 0);
        assertThrows(IllegalArgumentException.class, () ->
                zeroFrame.updateScore(0, Frame.MAX_PINS + 1));
    }

    @Test
    void compare_equalFrames_thenReturnTrue() {
        final int nbrPins1 = 2;
        final int nbrPins2 = 0;
        final DefaultFrame openFrame1 = DefaultFrame.nonStrike(nbrPins1, nbrPins2);
        final DefaultFrame openFrame2 = DefaultFrame.nonStrike(nbrPins1, nbrPins2);
        assertEquals(openFrame1, openFrame2);
    }

    @Test
    void compare_unequalFrames_thenReturnFalse() {
        final int nbrPins1 = 1;
        final DefaultFrame openFrame1 = DefaultFrame.nonStrike(nbrPins1, 1);
        final DefaultFrame openFrame2 = DefaultFrame.nonStrike(nbrPins1, 2);
        assertNotEquals(openFrame1, openFrame2);
    }
}

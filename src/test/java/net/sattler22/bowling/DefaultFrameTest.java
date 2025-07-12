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
 * @version July 2025
 */
final class DefaultFrameTest {

    @Test
    void recordStrike_thenFirstAttemptHasMaxPins() {
        final DefaultFrame strikeFrame = DefaultFrame.strike();
        assertEquals(Frame.MAX_PINS, strikeFrame.firstAttempt());
        assertEquals(0, strikeFrame.secondAttempt());
        assertEquals(Frame.MAX_PINS, strikeFrame.total());
        assertTrue(strikeFrame.isStrike());
        assertFalse(strikeFrame.isSpare());
        assertFalse(strikeFrame.isOpen());
        assertFalse(strikeFrame.isZero());
        assertFalse(strikeFrame.hasScore());
        assertTrue(strikeFrame.score().isEmpty());
    }

    @Test
    void recordSpare_thenTotalHasMaxPins() {
        final int nbrPins1 = 8;
        final DefaultFrame spareFrame = DefaultFrame.spare(nbrPins1);
        assertEquals(nbrPins1, spareFrame.firstAttempt());
        assertEquals(Frame.MAX_PINS - nbrPins1, spareFrame.secondAttempt());
        assertEquals(Frame.MAX_PINS, spareFrame.total());
        assertFalse(spareFrame.isStrike());
        assertTrue(spareFrame.isSpare());
        assertFalse(spareFrame.isOpen());
        assertFalse(spareFrame.isZero());
        assertFalse(spareFrame.hasScore());
        assertTrue(spareFrame.score().isEmpty());
    }

    @Test
    void recordSpare_withMaxFirstAttemptPins_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                DefaultFrame.spare(Frame.MAX_PINS)
        );
    }

    @Test
    void recordOpenFrame_thenTotalHasLessThanMaxPins() {
        final int nbrPins1 = 2;
        final int nbrPins2 = 0;
        final int expectedTotal = nbrPins1 + nbrPins2;
        final DefaultFrame openFrame = DefaultFrame.open(nbrPins1, nbrPins2);
        assertEquals(nbrPins1, openFrame.firstAttempt());
        assertEquals(nbrPins2, openFrame.secondAttempt());
        assertEquals(expectedTotal, openFrame.total());
        assertFalse(openFrame.isStrike());
        assertFalse(openFrame.isSpare());
        assertTrue(openFrame.isOpen());
        assertFalse(openFrame.isZero());
        assertFalse(openFrame.hasScore());
        assertTrue(openFrame.score().isEmpty());
    }

    @Test
    void recordOpenFrame_withMoreThanMaxPins_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                DefaultFrame.open(Frame.MAX_PINS, 1)
        );
    }

    @Test
    void recordOpenFrame_withTooManyPins_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                DefaultFrame.open(Frame.MAX_PINS, 0)
        );
    }

    @Test
    void recordZeroFrame_thenTotalHasNoPins() {
        final DefaultFrame zeroFrame = DefaultFrame.open(0, 0);
        assertEquals(0, zeroFrame.firstAttempt());
        assertEquals(0, zeroFrame.secondAttempt());
        assertEquals(0, zeroFrame.total());
        assertFalse(zeroFrame.isStrike());
        assertFalse(zeroFrame.isSpare());
        assertTrue(zeroFrame.isOpen());
        assertTrue(zeroFrame.isZero());
        assertFalse(zeroFrame.hasScore());
        assertTrue(zeroFrame.score().isEmpty());
    }

    @Test
    void score_withoutUpdate_thenReturnEmpty() {
        final DefaultFrame strikeFrame = DefaultFrame.strike();
        assertFalse(strikeFrame.hasScore());
        assertTrue(strikeFrame.score().isEmpty());
    }

    @Test
    void score_withUpdate_thenReturnScore() {
        final int nbrPins1 = 6;
        final int bonusPins = 3;
        final OptionalInt expectedScore = OptionalInt.of(Frame.MAX_PINS + bonusPins);
        final DefaultFrame spareFrame = DefaultFrame.spare(nbrPins1);
        spareFrame.updateScore(bonusPins);
        assertTrue(spareFrame.hasScore());
        assertEquals(expectedScore, spareFrame.score());
    }

    @Test
    void score_withNegativeBonus_thenThrowIllegalArgumentException() {
        final DefaultFrame zeroFrame = DefaultFrame.zero();
        assertThrows(IllegalArgumentException.class, () ->
                zeroFrame.updateScore(-1));
    }

    @Test
    void score_withBonusTooLarge_thenThrowIllegalArgumentException() {
        final DefaultFrame zeroFrame = DefaultFrame.zero();
        assertThrows(IllegalArgumentException.class, () ->
                zeroFrame.updateScore(Frame.MAX_PINS + 1));
    }

    @Test
    void compare_equalFrames_thenReturnTrue() {
        final int nbrPins1 = 2;
        final int nbrPins2 = 0;
        final DefaultFrame openFrame1 = DefaultFrame.open(nbrPins1, nbrPins2);
        final DefaultFrame openFrame2 = DefaultFrame.open(nbrPins1, nbrPins2);
        assertEquals(openFrame1, openFrame2);
    }

    @Test
    void compare_unequalFrames_thenReturnFalse() {
        final int nbrPins1 = 1;
        final DefaultFrame openFrame1 = DefaultFrame.open(nbrPins1, 1);
        final DefaultFrame openFrame2 = DefaultFrame.open(nbrPins1, 2);
        assertNotEquals(openFrame1, openFrame2);
    }
}

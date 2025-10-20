package net.sattler22.bowling.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ten Pin Bowling Final Frame Test Harness
 *
 * @author Pete Sattler
 * @since July 2025
 * @version October 2025
 */
final class FinalFrameTest {

    @Test
    void newInstance_withTooFewPins1_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(-1, 2)
        );
    }

    @Test
    void newInstance_withTooFewPins2_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(1, -1)
        );
    }

    @Test
    void newInstance_withTooManyPins1_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(Frame.MAX_PINS + 1, 2)
        );
    }

    @Test
    void newInstance_withTooManyPins2_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(1, Frame.MAX_PINS + 1)
        );
    }

    @Test
    void newInstance_withTooFewBonusPins_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(1, 2, -1)
        );
    }

    @Test
    void newInstance_withTooManyBonusPins_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(1, 2, Frame.MAX_PINS + 1)
        );
    }

    @Test
    void newInstance_withUnearnedBonusWithZeroPins_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(0, 0, 1)
        );
    }

    @Test
    void newInstance_withUnearnedBonusWithTooFewPins_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(1, 3, 4)
        );
    }

    @Test
    void newInstance_withSpare_thenSuccessful() {
        assertNewInstanceHappyPath(1, 9, 2);
    }

    @Test
    void newInstance_withFirstStrike_thenSuccessful() {
        assertNewInstanceHappyPath(Frame.MAX_PINS, 0, 4);
    }

    @Test
    void newInstance_withFirstStrikeWithSecondStrike_thenSuccessful() {
        assertNewInstanceHappyPath(Frame.MAX_PINS, Frame.MAX_PINS, 6);
    }

    @Test
    void newInstance_withTurkey_thenSuccessful() {
        assertNewInstanceHappyPath(Frame.MAX_PINS, Frame.MAX_PINS, Frame.MAX_PINS);
    }

    @Test
    void copyInstance_withHappyPath_thenSuccessful() {
        final Frame frame = spareFrame(4, 3);
        final Frame frameCopy = Frame.copyOf(frame);
        assertInstanceOf(FinalFrame.class, frameCopy);
        assertEquals(frame, frameCopy);
        assertNotSame(frameCopy, frame);
    }

    @Test
    void hasEarnedBonusRoll_withZeroPins_thenReturnFalse() {
        assertFalse(FinalFrame.hasEarnedBonusRoll(0, 0));
    }

    @Test
    void hasEarnedBonusRoll_withTooFewPins_thenReturnFalse() {
        assertFalse(FinalFrame.hasEarnedBonusRoll(0, 9));
    }

    @Test
    void hasEarnedBonusRoll_withSpare_thenReturnTrue() {
        assertTrue(FinalFrame.hasEarnedBonusRoll(4, 6));
    }

    @Test
    void hasEarnedBonusRoll_withFirstStrike_thenReturnTrue() {
        assertTrue(FinalFrame.hasEarnedBonusRoll(Frame.MAX_PINS, 0));
    }

    @Test
    void isZero_withTooManyPins_thenReturnFalse() {
        assertFalse(new FinalFrame(1, 0).isZero());
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
        assertTrue(new FinalFrame(1, 6).isOpen());
    }

    @Test
    void isOpen_withSpare_thenReturnFalse() {
        assertFalse(spareFrame(5, 1).isOpen());
    }

    @Test
    void isOpen_withFirstStrike_thenReturnFalse() {
        assertFalse(new FinalFrame(Frame.MAX_PINS, 0, 8).isOpen());
    }

    @Test
    void isOpen_withSecondStrike_thenReturnFalse() {
        assertFalse(new FinalFrame(0, Frame.MAX_PINS, 2).isOpen());
    }

    @Test
    void isOpen_withFirstStrikeWithSecondStrike_thenReturnFalse() {
        assertFalse(new FinalFrame(Frame.MAX_PINS, Frame.MAX_PINS, 3).isOpen());
    }

    @Test
    void isOpen_withTurkey_thenReturnFalse() {
        assertFalse(new FinalFrame(Frame.MAX_PINS, Frame.MAX_PINS, Frame.MAX_PINS).isOpen());
    }

    @Test
    void isSpare_withZeroPins_thenReturnFalse() {
        assertFalse(zeroFrame().isSpare());
    }

    @Test
    void isSpare_withTooFewPins_thenReturnFalse() {
        assertFalse(new FinalFrame(3, 5).isSpare());
    }

    @Test
    void isSpare_withHappyPath_thenReturnTrue() {
        assertTrue(spareFrame(7, 6).isSpare());
    }

    @Test
    void isSpare_withFirstStrike_thenReturnFalse() {
        assertFalse(new FinalFrame(Frame.MAX_PINS, 0, 2).isSpare());
    }

    @Test
    void isSpare_withSecondStrike_thenReturnTrue() {
        assertTrue(new FinalFrame(0, Frame.MAX_PINS, 3).isSpare());
    }

    @Test
    void isSpare_withFirstStrikeWithSecondStrike_thenReturnFalse() {
        assertFalse(new FinalFrame(Frame.MAX_PINS, Frame.MAX_PINS, 4).isSpare());
    }

    @Test
    void isSpare_withTurkey_thenReturnFalse() {
        assertFalse(new FinalFrame(Frame.MAX_PINS, Frame.MAX_PINS, Frame.MAX_PINS).isSpare());
    }

    @Test
    void isStrike_withZeroPins_thenReturnFalse() {
        assertFalse(zeroFrame().isStrike());
    }

    @Test
    void isStrike_withTooFewPins_thenReturnFalse() {
        assertFalse(new FinalFrame(4, 4).isStrike());
    }

    @Test
    void isStrike_withSpare_thenReturnFalse() {
        assertFalse(spareFrame(6, 2).isStrike());
    }

    @Test
    void isStrike_withFirstStrike_thenReturnTrue() {
        assertTrue(new FinalFrame(Frame.MAX_PINS, 0, 9).isStrike());
    }

    @Test
    void isStrike_withSecondStrike_thenReturnFalse() {
        assertFalse(new FinalFrame(0, Frame.MAX_PINS, 4).isStrike());
    }

    @Test
    void isStrike_withFirstStrikeWithSecondStrike_thenReturnTrue() {
        assertTrue(new FinalFrame(Frame.MAX_PINS, Frame.MAX_PINS, 6).isStrike());
    }

    @Test
    void isStrike_withTurkey_thenReturnTrue() {
        assertTrue(new FinalFrame(Frame.MAX_PINS, Frame.MAX_PINS, Frame.MAX_PINS).isStrike());
    }

    @Test
    void isTurkey_withZeroPins_thenReturnFalse() {
        assertFalse(zeroFrame().isTurkey());
    }

    @Test
    void isTurkey_withTooFewPins_thenReturnFalse() {
        assertFalse(new FinalFrame(1, 5).isTurkey());
    }

    @Test
    void isTurkey_withSpare_thenReturnFalse() {
        assertFalse(spareFrame(4, Frame.MAX_PINS).isTurkey());
    }

    @Test
    void isTurkey_withFirstStrike_thenReturnFalse() {
        assertFalse(new FinalFrame(Frame.MAX_PINS, 2, 5).isTurkey());
    }

    @Test
    void isTurkey_withSecondStrike_thenReturnFalse() {
        assertFalse(new FinalFrame(0, Frame.MAX_PINS, 9).isTurkey());
    }

    @Test
    void isTurkey_withFirstStrikeWithSecondStrike_thenReturnFalse() {
        assertFalse(new FinalFrame(Frame.MAX_PINS, Frame.MAX_PINS, 2).isTurkey());
    }

    @Test
    void isTurkey_withHappyPath_thenReturnTrue() {
        assertTrue(new FinalFrame(Frame.MAX_PINS, Frame.MAX_PINS, Frame.MAX_PINS).isTurkey());
    }

    @Test
    void hasScore_withoutUpdate_thenReturnFalse() {
        assertFalse(zeroFrame().hasScore());
    }

    @Test
    void hasScore_withHappyPath_thenReturnTrue() {
        final FinalFrame zeroFrame = zeroFrame();
        zeroFrame.updateScore(0);
        assertTrue(zeroFrame.hasScore());
    }

    @Test
    void score_withoutUpdate_thenReturnEmpty() {
        assertTrue(zeroFrame().score().isEmpty());
    }

    @Test
    void score_withHappyPath_thenReturnTrue() {
        final FinalFrame zeroFrame = zeroFrame();
        zeroFrame.updateScore(0);
        assertEquals(0, zeroFrame.score().orElse(-1));
    }

    @Test
    void updateScore_withNegativeStartingPoints_thenThrowIllegalArgumentException() {
        final FinalFrame zeroFrame = zeroFrame();
        assertThrows(IllegalArgumentException.class, () ->
                zeroFrame.updateScore(-1));
    }

    @Test
    void updateScore_withExistingScore_thenThrowIllegalStateException() {
        final FinalFrame zeroFrame = zeroFrame();
        zeroFrame.updateScore(0);
        assertThrows(IllegalStateException.class, () ->
                zeroFrame.updateScore(0));
    }

    @Test
    void updateScore_withHappyPath_thenSuccessful() {
        final int nbrPins1 = 6;
        final int nbrPins2 = 4;
        final int bonusNbrPins = 8;
        final int start = 35;
        final FinalFrame openFrame = new FinalFrame(nbrPins1, nbrPins2, bonusNbrPins);
        openFrame.updateScore(start);
        assertEquals(nbrPins1 + nbrPins2 + start + bonusNbrPins, openFrame.score().orElse(-1));
    }

    @Test
    void total_withHappyPath_thenSuccessful() {
        final int nbrPins1 = 7;
        final int nbrPins2 = 3;
        final int bonusNbrPins = 5;
        final FinalFrame finalFrame = new FinalFrame(nbrPins1, nbrPins2, bonusNbrPins);
        assertEquals(nbrPins1 + nbrPins2 + bonusNbrPins, finalFrame.total());
    }

    @Test
    void equals_whenSame_thenReturnTrue() {
        final FinalFrame spareFrame1 = spareFrame(1, 5);
        final FinalFrame spareFrame2 = spareFrame(1, 5);
        assertEquals(spareFrame1, spareFrame2);
    }

    @Test
    void equals_whenDifferent_thenReturnFalse() {
        final FinalFrame openFrame1 = new FinalFrame(1, 1);
        final FinalFrame openFrame2 = new FinalFrame(1, 2);
        assertNotEquals(openFrame1, openFrame2);
    }

    private static FinalFrame zeroFrame() {
        return new FinalFrame(0, 0);
    }

    private static FinalFrame spareFrame(int nbrPins1, int bonusNbrPins) {
        return new FinalFrame(nbrPins1, Frame.MAX_PINS - nbrPins1, bonusNbrPins);
    }

    private static void assertNewInstanceHappyPath(int nbrPins1, int nbrPins2, int bonusNbrPins) {
        final FinalFrame finalFrame = new FinalFrame(nbrPins1, nbrPins2, bonusNbrPins);
        assertEquals(nbrPins1, finalFrame.firstRoll());
        assertEquals(nbrPins2, finalFrame.secondRoll());
        assertEquals(bonusNbrPins, finalFrame.bonusRoll());
    }
}

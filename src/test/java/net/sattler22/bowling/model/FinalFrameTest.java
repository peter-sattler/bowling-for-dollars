package net.sattler22.bowling.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

    //TODO: newInstance

    @Test
    void newInstance_withTooFewPins1_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(-1, 2, 3)
        );
    }

    @Test
    void newInstance_withTooFewPins2_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(1, -1, 3)
        );
    }

    @Test
    void newInstance_withTooFewBonusPins_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(1, 2, -1)
        );
    }

    @Test
    void newInstance_withTooManyPins1_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(Frame.MAX_PINS + 1, 2, 3)
        );
    }

    @Test
    void newInstance_withTooManyPins2_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(1, Frame.MAX_PINS + 1, 3)
        );
    }

    @Test
    void newInstance_withTooManyBonusPins_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(1, 2, Frame.MAX_PINS + 1)
        );
    }

    @Test
    void newInstance_withZeroPinsWithBonus_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(0, 0, 1)
        );
    }

    @Test
    void newInstance_withTooFewPinsWithBonus_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(1, 3, 4)
        );
    }

    @Test
    void newInstance_withFirstStrikeWithBonus_thenSuccessful() {
        assertNewInstanceHappyPath(Frame.MAX_PINS, 0, 5);
    }

    @Test
    void newInstance_withSpareWithBonus_thenSuccessful() {
        assertNewInstanceHappyPath(3, 7, 8);
    }

    //TODO: hasEarnedBonusRoll

    @Test
    void hasEarnedBonusRoll_withNoFirstStrikeNoSpare_thenReturnFalse() {
        assertFalse(FinalFrame.hasEarnedBonusRoll(0, 0));
    }

    @Test
    void hasEarnedBonusRoll_withSpare_thenReturnTrue() {
        assertTrue(FinalFrame.hasEarnedBonusRoll(4, 6));
    }

    @Test
    void hasEarnedBonusRoll_withFirstStrike_thenReturnTrue() {
        assertTrue(FinalFrame.hasEarnedBonusRoll(Frame.MAX_PINS, 0));
    }

    //TODO: isZero

    @Test
    void isZero_withTooManyPins_thenReturnFalse() {
        assertFalse(new FinalFrame(1, 0, 0).isZero());
    }

    @Test
    void isZero_withHappyPath_thenReturnTrue() {
        assertTrue(zeroFrame().isZero());
    }

    //TODO: isOpen

    @Test
    void isOpen_withSpareNoBonus_thenReturnFalse() {
        assertFalse(new FinalFrame(5, 5, 0).isOpen());
    }

    @Test
    void isOpen_withSpareWithBonus_thenReturnFalse() {
        assertFalse(new FinalFrame(5, 5, 1).isOpen());
    }

    @Test
    void isOpen_withFirstStrikeNoBonus_thenReturnFalse() {
        assertFalse(new FinalFrame(Frame.MAX_PINS, 0, 0).isOpen());
    }

    @Test
    void isOpen_withFirstStrikeWithBonus_thenReturnFalse() {
        assertFalse(new FinalFrame(Frame.MAX_PINS, 0, 1).isOpen());
    }

    @Test
    void isOpen_withSecondStrikeNoBonus_thenReturnFalse() {
        assertFalse(new FinalFrame(0, Frame.MAX_PINS, 0).isOpen());
    }

    @Test
    void isOpen_withSecondStrikeWithBonus_thenReturnFalse() {
        assertFalse(new FinalFrame(0, Frame.MAX_PINS, 2).isOpen());
    }

    @Test
    void isOpen_withFirstStrikeWithSecondStrikeNoBonus_thenReturnFalse() {
        assertFalse(new FinalFrame(Frame.MAX_PINS, Frame.MAX_PINS, 0).isOpen());
    }

    @Test
    void isOpen_withFirstStrikeWithSecondStrikeWithBonus_thenReturnFalse() {
        assertFalse(new FinalFrame(Frame.MAX_PINS, Frame.MAX_PINS, 3).isOpen());
    }

    @Test
    void isOpen_withTurkey_thenReturnFalse() {
        assertFalse(new FinalFrame(Frame.MAX_PINS, Frame.MAX_PINS, Frame.MAX_PINS).isOpen());
    }

    @Test
    void isOpen_withZeroPins_thenReturnTrue() {
        assertTrue(zeroFrame().isOpen());
    }

    @Test
    void isOpen_withHappyPath_thenReturnTrue() {
        assertTrue(new FinalFrame(1, 6, 0).isOpen());
    }

    //TODO: isSpare

    //TODO: isStrike
    //TODO: isTurkey
    //TODO: hasScore
    //TODO: score
    //TODO: updateScore
    //TODO: firstRoll
    //TODO: secondRoll
    //TODO: bonusRoll
    //TODO: total

    @Test
    void isTurkey_withFirstStrike_thenReturnFalse() {
        assertFalse(new FinalFrame(Frame.MAX_PINS, 2, 0).isTurkey());
    }

    @Test
    void isTurkey_withTwoStrikes_thenReturnFalse() {
        assertFalse(new FinalFrame(Frame.MAX_PINS, Frame.MAX_PINS, 2).isTurkey());
    }

    @Test
    void isTurkey_withThreeStrikes_thenReturnTrue() {
        assertTrue(new FinalFrame(Frame.MAX_PINS, Frame.MAX_PINS, Frame.MAX_PINS).isTurkey());
    }

    //TODO: equals

    @Test
    void equals_whenSame_thenReturnFalse() {
        final int nbrPins1 = 1;
        final int nbrPins2 = 9;
        final int bonusNbrPins = 5;
        final FinalFrame finalFrame1 = new FinalFrame(nbrPins1, nbrPins2, bonusNbrPins);
        final FinalFrame finalFrame2 = new FinalFrame(nbrPins1, nbrPins2, bonusNbrPins);
        assertEquals(finalFrame1, finalFrame2);
    }

    @Test
    void equals_whenDifferent_thenReturnFalse() {
        final int nbrPins1 = Frame.MAX_PINS;
        final int nbrPins2 = 2;
        final FinalFrame finalFrame1 = new FinalFrame(nbrPins1, nbrPins2, 1);
        final FinalFrame finalFrame2 = new FinalFrame(nbrPins1, nbrPins2, 2);
        assertNotEquals(finalFrame1, finalFrame2);
    }

    private static FinalFrame zeroFrame() {
        return new FinalFrame(0, 0, 0);
    }

    private static void assertNewInstanceHappyPath(int nbrPins1, int nbrPins2, int bonusNbrPins) {
        final FinalFrame finalFrame = new FinalFrame(nbrPins1, nbrPins2, bonusNbrPins);
        assertEquals(nbrPins1, finalFrame.firstRoll());
        assertEquals(nbrPins2, finalFrame.secondRoll());
        assertEquals(bonusNbrPins, finalFrame.bonusRoll());
    }
}

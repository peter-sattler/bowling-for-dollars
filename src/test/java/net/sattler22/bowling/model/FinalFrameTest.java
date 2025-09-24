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

    @Test
    void newInstance_withTooManyPins1_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
            new FinalFrame(Frame.MAX_PINS + 1, 0, 0));
    }

    @Test
    void newInstance_withTooManyPins2_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(0, Frame.MAX_PINS + 1, 0));
    }

    @Test
    void newInstance_withTooManyBonusPins_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(0, 0, Frame.MAX_PINS + 1));
    }

    @Test
    void newInstance_withUnearnedBonusNoStrike_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(Frame.MAX_PINS - 1, 0, 1));
    }

    @Test
    void newInstance_withUnearnedBonusNoSpare_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new FinalFrame(1, 8, 1));
    }

    @Test
    void newInstance_withFirstStrikeBonusEarned_thenSuccessful() {
        final int nbrPins1 = Frame.MAX_PINS;
        final int nbrPins2 = 2;
        final int bonusNbrPins = 6;
        final FinalFrame finalFrame = new FinalFrame(nbrPins1, nbrPins2, bonusNbrPins);
        assertEquals(nbrPins1, finalFrame.firstRoll());
        assertEquals(nbrPins2, finalFrame.secondRoll());
        assertEquals(bonusNbrPins, finalFrame.bonusRoll());
        assertFalse(finalFrame.isTurkey());
        assertEquals(nbrPins1 + nbrPins2 + bonusNbrPins, finalFrame.total());
    }

    @Test
    void newInstance_withSpareBonusEarned_thenSuccessful() {
        final int nbrPins1 = Frame.MAX_PINS - 2;
        final int nbrPins2 = 2;
        final int bonusNbrPins = 5;
        final FinalFrame finalFrame = new FinalFrame(nbrPins1, nbrPins2, bonusNbrPins);
        assertEquals(nbrPins1, finalFrame.firstRoll());
        assertEquals(nbrPins2, finalFrame.secondRoll());
        assertEquals(bonusNbrPins, finalFrame.bonusRoll());
        assertFalse(finalFrame.isTurkey());
        assertEquals(nbrPins1 + nbrPins2 + bonusNbrPins, finalFrame.total());
    }

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

    @Test
    void isSpare_withFirstStrike_thenReturnFalse() {
        assertFalse(new FinalFrame(Frame.MAX_PINS, 2, 4).isSpare());
    }

    @Test
    void isSpare_withOpenFrameNoBonus_thenReturnFalse() {
        assertFalse(new FinalFrame(1, 2, 0).isSpare());
    }

    @Test
    void isSpare_withSpareNoBonus_thenReturnTrue() {
        assertTrue(new FinalFrame(9, 1, 0).isSpare());
    }

    @Test
    void isSpare_withSpareAndBonus_thenReturnTrue() {
        assertTrue(new FinalFrame(9, 1, Frame.MAX_PINS).isSpare());
    }

    @Test
    void compare_equalFrames_thenReturnTrue() {
        final int nbrPins1 = 1;
        final int nbrPins2 = 9;
        final int bonusNbrPins = 5;
        final FinalFrame finalFrame1 = new FinalFrame(nbrPins1, nbrPins2, bonusNbrPins);
        final FinalFrame finalFrame2 = new FinalFrame(nbrPins1, nbrPins2, bonusNbrPins);
        assertEquals(finalFrame1, finalFrame2);
    }

    @Test
    void compare_unequalFrames_thenReturnFalse() {
        final int nbrPins1 = Frame.MAX_PINS;
        final int nbrPins2 = 2;
        final FinalFrame finalFrame1 = new FinalFrame(nbrPins1, nbrPins2, 1);
        final FinalFrame finalFrame2 = new FinalFrame(nbrPins1, nbrPins2, 2);
        assertNotEquals(finalFrame1, finalFrame2);
    }
}

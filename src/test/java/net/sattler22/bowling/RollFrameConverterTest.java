package net.sattler22.bowling;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ten Pin Bowling Roll to Frame Converter Test Harness
 *
 * @author Pete Sattler
 * @version August 2025
 */
final class RollFrameConverterTest {

    @Test
    void roll_withMinimumPins_thenSuccessful() {
        final int nbrPins1 = 6;
        final RollFrameConverter rollFrameConverter = createWithOneRollImpl(nbrPins1);
        assertEquals(nbrPins1, rollFrameConverter.total());
        assertEquals(1, rollFrameConverter.size());
    }

    @Test
    void roll_withMaxPins_thenSuccessful() {
        final RollFrameConverter rollFrameConverter = createWithOneRollImpl(Frame.MAX_PINS);
        assertEquals(Frame.MAX_PINS, rollFrameConverter.total());
        assertEquals(1, rollFrameConverter.size());
    }

    @Test
    void roll_withTooFewPins_thenThrowIllegalArgumentException() {
        final RollFrameConverter rollFrameConverter = new RollFrameConverter();
        assertThrows(IllegalArgumentException.class, () ->
                rollFrameConverter.roll(-1));
    }

    @Test
    void roll_withTooManyPins_thenThrowIllegalArgumentException() {
        final RollFrameConverter rollFrameConverter = new RollFrameConverter();
        assertThrows(IllegalArgumentException.class, () ->
                rollFrameConverter.roll(Frame.MAX_PINS + 1));
    }

    @Test
    void convertDefaultFrame_withNoRolls_thenReturnEmpty() {
        final RollFrameConverter rollFrameConverter = new RollFrameConverter();
        final Optional<Frame> emptyFrame = rollFrameConverter.convert(false);
        assertTrue(emptyFrame.isEmpty());
        assertEquals(0, rollFrameConverter.total());
        assertEquals(0, rollFrameConverter.size());
    }

    @Test
    void convertDefaultFrame_withOneNonStrike_thenReturnEmpty() {
        final int nbrPins1 = Frame.MAX_PINS - 1;
        final RollFrameConverter rollFrameConverter = createWithOneRollImpl(nbrPins1);
        final Optional<Frame> emptyFrame = rollFrameConverter.convert(false);
        assertTrue(emptyFrame.isEmpty());
        assertEquals(nbrPins1, rollFrameConverter.total());
        assertEquals(1, rollFrameConverter.size());
    }

    @Test
    void convertDefaultFrame_withStrike_thenReturnStrikeFrame() {
        final RollFrameConverter rollFrameConverter = createWithOneRollImpl(Frame.MAX_PINS);
        final DefaultFrame strikeFrame =
                (DefaultFrame) rollFrameConverter.convert(false)
                        .orElse(null);
        assertNotNull(strikeFrame);
        assertTrue(strikeFrame.isStrike());
        assertEquals(0, rollFrameConverter.total());
        assertEquals(0, rollFrameConverter.size());
    }

    @Test
    void convertDefaultFrame_withTwoNonStrikes_thenReturnNonStrikeFrame() {
        final RollFrameConverter rollFrameConverter = createWithOneRollImpl(3);
        rollFrameConverter.roll(1);
        final DefaultFrame nonStrikeFrame =
                (DefaultFrame) rollFrameConverter.convert(false)
                        .orElse(null);
        assertNotNull(nonStrikeFrame);
        assertFalse(nonStrikeFrame.isStrike());
        assertEquals(0, rollFrameConverter.total());
        assertEquals(0, rollFrameConverter.size());
    }

    @Test
    void convertToFinalFrame_withNoRolls_thenReturnEmpty() {
        final RollFrameConverter rollFrameConverter = new RollFrameConverter();
        final Optional<Frame> emptyFrame = rollFrameConverter.convert(true);
        assertTrue(emptyFrame.isEmpty());
        assertEquals(0, rollFrameConverter.total());
        assertEquals(0, rollFrameConverter.size());
    }

    @Test
    void convertFinalFrame_withFirstStrike_thenReturnEmpty() {
        final RollFrameConverter rollFrameConverter = createWithOneRollImpl(Frame.MAX_PINS);
        final Optional<Frame> emptyFrame = rollFrameConverter.convert(true);
        assertTrue(emptyFrame.isEmpty());
        assertEquals(Frame.MAX_PINS, rollFrameConverter.total());
        assertEquals(1, rollFrameConverter.size());
    }

    @Test
    void convertFinalFrame_withTwoStrikes_thenReturnEmpty() {
        final RollFrameConverter rollFrameConverter = createWithOneRollImpl(Frame.MAX_PINS);
        rollFrameConverter.roll(Frame.MAX_PINS);
        final Optional<Frame> emptyFrame = rollFrameConverter.convert(true);
        assertTrue(emptyFrame.isEmpty());
        assertEquals(Frame.MAX_PINS * 2, rollFrameConverter.total());
        assertEquals(2, rollFrameConverter.size());
    }

    @Test
    void convertFinalFrame_withThreeStrikes_thenReturnFinalFrame() {
        final RollFrameConverter rollFrameConverter = createWithOneRollImpl(Frame.MAX_PINS);
        rollFrameConverter.roll(Frame.MAX_PINS);
        rollFrameConverter.roll(Frame.MAX_PINS);
        final FinalFrame finalFrame =
                (FinalFrame) rollFrameConverter.convert(true)
                        .orElse(null);
        assertNotNull(finalFrame);
        assertTrue(finalFrame.isTurkey());
        assertEquals(0, rollFrameConverter.total());
        assertEquals(0, rollFrameConverter.size());
    }

    @Test
    void convertFinalFrame_withFirstStrikeOneNonStrike_thenReturnEmpty() {
        final int nbrPins2 = 3;
        final RollFrameConverter rollFrameConverter = createWithOneRollImpl(Frame.MAX_PINS);
        rollFrameConverter.roll(nbrPins2);
        final Optional<Frame> emptyFrame = rollFrameConverter.convert(true);
        assertTrue(emptyFrame.isEmpty());
        assertEquals(Frame.MAX_PINS + nbrPins2, rollFrameConverter.total());
        assertEquals(2, rollFrameConverter.size());
    }

    @Test
    void convertFinalFrame_withFirstStrikeTwoNonStrikes_thenReturnFinalFrame() {
        final RollFrameConverter rollFrameConverter = createWithOneRollImpl(Frame.MAX_PINS);
        rollFrameConverter.roll(3);
        rollFrameConverter.roll(1);
        final Optional<Frame> finalFrame = rollFrameConverter.convert(true);
        assertFalse(finalFrame.isEmpty());
        assertEquals(0, rollFrameConverter.total());
        assertEquals(0, rollFrameConverter.size());
    }

    @Test
    void convertFinalFrame_withFirstNonStrike_thenReturnEmpty() {
        final int nbrPins1 = 8;
        final RollFrameConverter rollFrameConverter = createWithOneRollImpl(nbrPins1);
        final Optional<Frame> emptyFrame = rollFrameConverter.convert(true);
        assertTrue(emptyFrame.isEmpty());
        assertEquals(nbrPins1, rollFrameConverter.total());
        assertEquals(1, rollFrameConverter.size());
    }

    @Test
    void convertFinalFrame_withTwoNonStrikes_thenFinalFrame() {
        final int nbrPins1 = 5;
        final int nbrPins2 = 2;
        final RollFrameConverter rollFrameConverter = createWithOneRollImpl(nbrPins1);
        rollFrameConverter.roll(nbrPins2);
        final Optional<Frame> finalFrame = rollFrameConverter.convert(true);
        assertFalse(finalFrame.isEmpty());
        assertEquals(0, rollFrameConverter.total());
        assertEquals(0, rollFrameConverter.size());
    }

    @Test
    void convertFinalFrame_withSpareNoBonus_thenReturnEmpty() {
        final int nbrPins1 = 7;
        final int nbrPins2 = 3;
        final RollFrameConverter rollFrameConverter = createWithOneRollImpl(nbrPins1);
        rollFrameConverter.roll(nbrPins2);
        final Optional<Frame> emptyFrame = rollFrameConverter.convert(true);
        assertTrue(emptyFrame.isEmpty());
        assertEquals(nbrPins1 + nbrPins2, rollFrameConverter.total());
        assertEquals(2, rollFrameConverter.size());
    }

    @Test
    void convertFinalFrame_withSpareAndBonus_thenReturnFinalFrame() {
        final int nbrPins1 = 7;
        final int nbrPins2 = 3;
        final int nbrPins3 = 8;
        final RollFrameConverter rollFrameConverter = createWithOneRollImpl(nbrPins1);
        rollFrameConverter.roll(nbrPins2);
        rollFrameConverter.roll(nbrPins3);
        final Optional<Frame> finalFrame = rollFrameConverter.convert(true);
        assertFalse(finalFrame.isEmpty());
        assertEquals(0, rollFrameConverter.total());
        assertEquals(0, rollFrameConverter.size());
    }

    private static RollFrameConverter createWithOneRollImpl(int nbrPins) {
        final RollFrameConverter rollFrameConverter = new RollFrameConverter();
        rollFrameConverter.roll(nbrPins);
        return rollFrameConverter;
    }
}

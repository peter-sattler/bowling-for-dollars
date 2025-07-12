package net.sattler22.bowling;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ten Pin Bowling Roll Tracker Test Harness
 *
 * @author Pete Sattler
 * @version July 2025
 */
final class RollTrackerTest {

    @Test
    void roll_withMinimumPins_thenSuccessful() {
        createWithOneRollAssertImpl(0);
    }

    @Test
    void roll_withMaxPins_thenSuccessful() {
        createWithOneRollAssertImpl(Frame.MAX_PINS);
    }

    @Test
    void roll_withTooFewPins_thenThrowIllegalArgumentException() {
        final RollTracker rollTracker = new RollTracker();
        assertThrows(IllegalArgumentException.class, () ->
                rollTracker.add(-1));
    }

    @Test
    void roll_withTooManyPins_thenThrowIllegalArgumentException() {
        final RollTracker rollTracker = new RollTracker();
        assertThrows(IllegalArgumentException.class, () ->
                rollTracker.add(Frame.MAX_PINS + 1));
    }

    @Test
    void getNext_WithOneRoll_thenSuccessful() {
        createWithOneRollAssertImpl(5);
    }

    @Test
    void getNext_withMultipleRolls_thenOrderIsFirstInFirstOut() {
        final int nbrPins1 = 7;
        final int nbrPins2 = 2;
        final RollTracker rollTracker = createWithOneRollImpl(nbrPins1);
        rollTracker.add(nbrPins2);
        assertEquals(nbrPins1, rollTracker.getNext());
        assertEquals(nbrPins2, rollTracker.getNext());
        assertTrue(rollTracker.isEmpty());
    }

    @Test
    void getNext_withEmptyQueue_thenThrowIllegalStateException() {
        final RollTracker rollTracker = new RollTracker();
        assertThrows(IllegalStateException.class, rollTracker::getNext);
    }

    @Test
    void nextRollIsStrike_withStrike_thenReturnTrueAndTrackerHasOneRoll() {
        final RollTracker rollTracker = createWithOneRollImpl(Frame.MAX_PINS);
        assertTrue(rollTracker.nextRollIsStrike());
        assertFalse(rollTracker.isEmpty());
    }

    @Test
    void nextRollIsStrike_withOneRoll_thenReturnFalseAndTrackerHasOneRoll() {
        final RollTracker rollTracker = createWithOneRollImpl(5);
        assertFalse(rollTracker.nextRollIsStrike());
        assertFalse(rollTracker.isEmpty());
        assertEquals(1, rollTracker.size());
    }

    @Test
    void nextRollIsStrike_withNoRolls_thenReturnFalseAndTrackerEmpty() {
        final RollTracker rollTracker = new RollTracker();
        assertFalse(rollTracker.nextRollIsStrike());
        assertTrue(rollTracker.isEmpty());
    }

    @Test
    void nextTwoRollsIsSpare_withSpare_thenReturnTrueAndTrackerHasTwoRolls() {
        final int nbrPins = 6;
        final RollTracker rollTracker = createWithOneRollImpl(nbrPins);
        rollTracker.add(Frame.MAX_PINS - nbrPins);
        assertTrue(rollTracker.nextTwoRollsIsSpare());
        assertFalse(rollTracker.isEmpty());
        assertEquals(2, rollTracker.size());
    }

    @Test
    void nextTwoRollsIsSpare_withStrike_thenReturnFalseAndTrackerHasOneRoll() {
        final RollTracker rollTracker = createWithOneRollImpl(Frame.MAX_PINS);
        assertFalse(rollTracker.nextTwoRollsIsSpare());
        assertFalse(rollTracker.isEmpty());
        assertEquals(1, rollTracker.size());
    }

    @Test
    void nextTwoRollsIsSpare_withOneNonStrikeRoll_thenReturnFalseAndTrackerHasOneRoll() {
        final RollTracker rollTracker = createWithOneRollImpl(3);
        assertFalse(rollTracker.nextTwoRollsIsSpare());
        assertFalse(rollTracker.isEmpty());
        assertEquals(1, rollTracker.size());
    }

    @Test
    void nextTwoRollsIsSpare_withNonSpare_thenReturnFalseAndTrackerHasTwoRolls() {
        final RollTracker rollTracker = createWithOneRollImpl(1);
        rollTracker.add(2);
        assertFalse(rollTracker.nextTwoRollsIsSpare());
        assertFalse(rollTracker.isEmpty());
        assertEquals(2, rollTracker.size());
    }

    @Test
    void nextTwoRollsIsSpare_withNoRolls_thenReturnFalseAndTrackerEmpty() {
        final RollTracker rollTracker = new RollTracker();
        assertFalse(rollTracker.nextTwoRollsIsSpare());
        assertTrue(rollTracker.isEmpty());
    }

    @Test
    void total_withNoRolls_thenReturnZeroSum() {
        final RollTracker rollTracker = new RollTracker();
        assertEquals(0, rollTracker.total());
    }

    @Test
    void total_withMultipleRolls_thenReturnExpectedSum() {
        final int nbrPins1 = 1;
        final int nbrPins2 = 2;
        final int expectedTotal = nbrPins1 + nbrPins2;
        final RollTracker rollTracker = createWithOneRollImpl(nbrPins1);
        rollTracker.add(nbrPins2);
        assertEquals(expectedTotal, rollTracker.total());
    }

    @Test
    void size_withNoRolls_thenReturnZeroSize() {
        final RollTracker rollTracker = new RollTracker();
        assertEquals(0, rollTracker.size());
    }

    @Test
    void size_withOneRoll_thenReturnExpectedSize() {
        final RollTracker rollTracker = createWithOneRollImpl(4);
        assertEquals(1, rollTracker.size());
    }

    @Test
    void size_withMultipleRolls_thenReturnExpectedSize() {
        final RollTracker rollTracker = createWithOneRollImpl(1);
        rollTracker.add(2);
        rollTracker.add(3);
        assertEquals(3, rollTracker.size());
    }

    @Test
    void isEmpty_withNoRolls_thenReturnTrue() {
        final RollTracker rollTracker = new RollTracker();
        assertTrue(rollTracker.isEmpty());
    }

    @Test
    void isEmpty_withOneRoll_thenReturnFalse() {
        final RollTracker rollTracker = createWithOneRollImpl(4);
        assertFalse(rollTracker.isEmpty());
    }

    private static void createWithOneRollAssertImpl(int nbrPins) {
        final RollTracker rollTracker = createWithOneRollImpl(nbrPins);
        assertEquals(nbrPins, rollTracker.getNext());
    }

    private static RollTracker createWithOneRollImpl(int nbrPins) {
        final RollTracker rollTracker = new RollTracker();
        rollTracker.add(nbrPins);
        return rollTracker;
    }
}

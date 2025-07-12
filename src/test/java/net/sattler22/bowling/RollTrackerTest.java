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
    void addRoll_withMinimumPins_thenSuccessful() {
        assertAddGetFirstImpl(0);
    }

    @Test
    void addRoll_withMaxPins_thenSuccessful() {
        assertAddGetFirstImpl(Frame.MAX_PINS);
    }

    @Test
    void addRoll_withTooFewPins_thenThrowIllegalArgumentException() {
        final RollTracker rollTracker = new RollTracker();
        assertThrows(IllegalArgumentException.class, () ->
                rollTracker.add(-1));
    }

    @Test
    void addRoll_withTooManyPins_thenThrowIllegalArgumentException() {
        final RollTracker rollTracker = new RollTracker();
        assertThrows(IllegalArgumentException.class, () ->
                rollTracker.add(Frame.MAX_PINS + 1));
    }

    @Test
    void getFirst_WithOneRoll_thenSuccessful() {
        assertAddGetFirstImpl(5);
    }

    @Test
    void getFirst_withMultipleRolls_thenOrderIsFirstInFirstOut() {
        final int nbrPins1 = 7;
        final int nbrPins2 = 2;
        final RollTracker rollTracker = createWithOneRollImpl(nbrPins1);
        rollTracker.add(nbrPins2);
        assertEquals(nbrPins1, rollTracker.getFirst());
        assertEquals(nbrPins2, rollTracker.getFirst());
        assertTrue(rollTracker.isEmpty());
    }

    @Test
    void getFirst_withEmptyQueue_thenThrowIllegalStateException() {
        final RollTracker rollTracker = new RollTracker();
        assertThrows(IllegalStateException.class, rollTracker::getFirst);
    }

    @Test
    void firstRollIsStrike_withStrike_thenReturnTrueAndTrackerEmpty() {
        final RollTracker rollTracker = createWithOneRollImpl(Frame.MAX_PINS);
        assertTrue(rollTracker.firstRollIsStrike());
        assertTrue(rollTracker.isEmpty());
    }

    @Test
    void firstRollIsStrike_withOneRoll_thenReturnFalseAndTrackerHasOneRoll() {
        final RollTracker rollTracker = createWithOneRollImpl(5);
        assertFalse(rollTracker.firstRollIsStrike());
        assertFalse(rollTracker.isEmpty());
        assertEquals(1, rollTracker.size());
    }

    @Test
    void firstRollIsStrike_withNoRolls_thenReturnFalseAndTrackerEmpty() {
        final RollTracker rollTracker = new RollTracker();
        assertFalse(rollTracker.firstRollIsStrike());
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

    private static void assertAddGetFirstImpl(int nbrPins) {
        final RollTracker rollTracker = createWithOneRollImpl(nbrPins);
        assertEquals(nbrPins, rollTracker.getFirst());
    }

    private static RollTracker createWithOneRollImpl(int nbrPins) {
        final RollTracker rollTracker = new RollTracker();
        rollTracker.add(nbrPins);
        return rollTracker;
    }
}

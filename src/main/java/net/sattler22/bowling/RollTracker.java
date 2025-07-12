package net.sattler22.bowling;

import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Ten Pin Bowling Roll Tracker
 * <p>
 * A holding area for rolls in a ten pin bowling game prior to being converted to a {@link Frame}. Rolls
 * are returned in a first in, first out (FIFO) order and are permanently removed once retrieved.
 * </p>
 *
 * @author Pete Sattler
 * @version July 2025
 */
@ThreadSafe
final class RollTracker {

    private final Queue<Integer> rollQueue = new LinkedList<>();
    private final Object lock = new Object();

    /**
     * Add a roll
     *
     * @param nbrPins The number of pins for this roll
     */
    void add(int nbrPins) {
        if (nbrPins < 0 || nbrPins > Frame.MAX_PINS)
            throw new IllegalArgumentException("Invalid number of pins");
        rollQueue.add(nbrPins);
    }

    /**
     * Get first roll
     *
     * @return The number of pins for the first roll
     */
    int getFirst() {
        if (rollQueue.isEmpty())
            throw new IllegalStateException("No rolls found");
        return rollQueue.poll();
    }

    /**
     * First roll strike condition check
     *
     * @return True if all pins have been knocked down in the first roll. Otherwise, returns false.
     */
    boolean firstRollIsStrike() {
        synchronized (lock) {
            if (rollQueue.isEmpty() || rollQueue.peek() != Frame.MAX_PINS)
                return false;
            rollQueue.remove();
            return true;
        }
    }

    /**
     * Get total
     *
     * @return The total number of pins for all rolls
     */
    int total() {
        return rollQueue.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    /**
     * Get size
     *
     * @return The number of rolls
     */
    int size() {
        return rollQueue.size();
    }

    /**
     * Empty condition check
     *
     * @return True if there are no rolls. Otherwise, returns false.
     */
    boolean isEmpty() {
        return rollQueue.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("%s %s", getClass().getSimpleName(), rollQueue);
    }
}

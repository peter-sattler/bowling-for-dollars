package net.sattler22.bowling.util;

import net.jcip.annotations.ThreadSafe;
import net.sattler22.bowling.model.Frame;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Ten Pin Bowling Roll Tracker
 * <p>
 * A holding area for rolls in a ten pin bowling game prior to being converted to a {@link Frame}. Rolls
 * are returned in a first in, first out (FIFO) order and are permanently removed once they are retrieved.
 * </p>
 *
 * @author Pete Sattler
 * @version August 2025
 */
@ThreadSafe
public final class RollTracker {

    private final Queue<Integer> rollQueue = new LinkedList<>();
    private final Object lock = new Object();

    /**
     * Add a roll
     *
     * @param nbrPins The number of pins for this roll
     */
    public void add(int nbrPins) {
        if (nbrPins < 0)
            throw new IllegalArgumentException("Invalid number of pins");
        if (nbrPins > Frame.MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins exceeded");
        rollQueue.add(nbrPins);
    }

    /**
     * Get and remove the next roll
     *
     * @return The number of pins for the first roll
     */
    public int getNext() {
        synchronized (lock) {
            if (rollQueue.isEmpty())
                throw new IllegalStateException("No rolls found");
            return rollQueue.poll();
        }
    }

    /**
     * Next roll strike condition check
     *
     * @return True if all pins have been knocked down in the next roll. Otherwise, returns false.
     */
    public boolean nextRollIsStrike() {
        synchronized (lock) {
            return !rollQueue.isEmpty() && rollQueue.peek() == Frame.MAX_PINS;
        }
    }

    /**
     * Next two rolls spare condition check
     *
     * @return True if all pins have been knocked down in the next two rolls. Otherwise, returns false.
     */
    public boolean nextTwoRollsIsSpare() {
        synchronized (lock) {
            if (rollQueue.size() < 2 || rollQueue.peek() == Frame.MAX_PINS)
                return false;
            return rollQueue.stream()
                    .limit(2)
                    .mapToInt(Integer::intValue)
                    .sum() == Frame.MAX_PINS;
        }
    }

    /**
     * Empty condition check
     *
     * @return True if there are no rolls. Otherwise, returns false.
     */
    public boolean isEmpty() {
        return rollQueue.isEmpty();
    }

    /**
     * Get size
     *
     * @return The number of rolls
     */
    public int size() {
        return rollQueue.size();
    }

    /**
     * Get total
     *
     * @return The total number of pins for all rolls
     */
    public int total() {
        return rollQueue.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Override
    public String toString() {
        return String.format("%s [rollQueue=%s]", getClass().getSimpleName(), rollQueue);
    }
}

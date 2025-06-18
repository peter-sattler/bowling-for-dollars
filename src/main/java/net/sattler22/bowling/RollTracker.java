package net.sattler22.bowling;

import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Ten Pin Bowling Roll Tracker
 * <p>
 * Tracks the rolls in a ten pin bowling game. The roll is permanently removed upon retrieval. Rolls are retrieved in
 * FIFO (first in, first out) order.
 * </p>
 *
 * @author Pete Sattler
 * @since June 2025
 * @version June 2025
 */
@ThreadSafe
public final class RollTracker {

    private final Queue<Integer> rollQueue = new LinkedList<>();

    /**
     * Add a roll
     *
     * @param nbrPins The number of pins knocked down
     */
    void add(int nbrPins) {
        if (nbrPins < 0 || nbrPins > Frame.MAX_PINS)
            throw new IllegalArgumentException("Invalid number of pins");
        rollQueue.add(nbrPins);
    }

    /**
     * Get next roll
     *
     * @return The next available roll, removing it permanently from the underlying data structure
     */
    int getNext() {
        if (rollQueue.isEmpty())
            throw new IllegalStateException("No rolls found");
        return rollQueue.remove();
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

    int size() {
        return rollQueue.size();
    }

    boolean isEmpty() {
        return rollQueue.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("%s %s", getClass().getSimpleName(), rollQueue);
    }
}

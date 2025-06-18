package net.sattler22.bowling;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A <code>Frame</code> represents a series of turns or opportunities for a bowler to throw the ball down the lane
 * and attempt to knock down the pins.
 *
 * @author Pete Sattler
 * @since June 2025
 * @version June 2025
 */
@ThreadSafe
abstract sealed class Frame permits DefaultFrame, FinalFrame {

    /**
     * Maximum pins per frame
     */
    protected static final int MAX_PINS = 10;

    private static final AtomicInteger frameNumberGenerator = new AtomicInteger(0);

    protected final int number;
    protected final int attempt1;
    protected final int attempt2;

    /**
     * Constructs a new <code>Frame</code>
     *
     * @param attempt1 The number of pins knocked down in the first attempt
     * @param attempt2 The number of pins knocked down in the second attempt
     */
    protected Frame(int attempt1, int attempt2) {
        this.number = frameNumberGenerator.incrementAndGet();
        this.attempt1 = attempt1;
        this.attempt2 = attempt2;
    }

    /**
     * Get frame number
     *
     * @return The frame number of this game
     */
    int number() {
        return number;
    }

    /**
     * Get first attempt
     *
     * @return The number of pins knocked down in the first attempt
     */
    int firstAttempt() {
        return attempt1;
    }

    /**
     * Get second attempt
     *
     * @return The number of pins knocked down in the second attempt
     */
    int secondAttempt() {
        return attempt2;
    }

    /**
     * Get total
     *
     * @return The total number of pins knocked down
     */
    int total() {
        return attempt1 + attempt2;
    }

    /**
     * Open frame condition check
     *
     * @return True if at least one pin is left standing after all possible attempts have been made. Otherwise, returns false.
     */
    boolean isOpen() {
        return total() < MAX_PINS;
    }

    /**
     * Zero frame condition check
     *
     * @return True if no pins have been knocked down after all possible attempts have been made. Otherwise, returns false.
     */
    boolean isZero() {
        return total() == 0;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(number);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (this.getClass() != other.getClass())
            return false;
        final Frame that = (Frame) other;
        return this.number == that.number;
    }

    @Override
    public String toString() {
        return String.format("%s [number=%d, attempt1=%s, attempt2=%s]", getClass().getSimpleName(), number, attempt1, attempt2);
    }
}

package net.sattler22.bowling;

import net.jcip.annotations.ThreadSafe;

/**
 * A Ten Pin Bowling <code>Frame</code> represents a series of opportunities for a bowler to throw the ball down the
 * lane and knock down the pins.
 *
 * @author Pete Sattler
 * @since July 2025
 * @version October 2025
 */
@ThreadSafe
public abstract sealed class Frame permits DefaultFrame, FinalFrame {

    /**
     * Maximum pins per frame
     */
    public static final int MAX_PINS = 10;

    private final int firstRoll;
    private final int secondRoll;
    private final boolean zero;
    private final boolean open;
    private final boolean spare;
    private final boolean strike;
    private volatile int score = -1;
    private final Object lock = new Object();

    /**
     * Constructs a new <code>Frame</code>
     *
     * @param nbrPins1 The number of pins knocked down in the first roll
     * @param nbrPins2 The number of pins knocked down in the second roll
     */
    protected Frame(int nbrPins1, int nbrPins2) {
        if (nbrPins1 < 0 || nbrPins2 < 0)
            throw new IllegalArgumentException("Invalid number of pins");
        if (nbrPins1 > MAX_PINS || nbrPins2 > MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins exceeded");
        this.firstRoll = nbrPins1;
        this.secondRoll = nbrPins2;
        this.zero = nbrPins1 + nbrPins2 == 0;
        this.open = nbrPins1 + nbrPins2 < MAX_PINS;  //Zero frame is also an open one
        this.strike = nbrPins1 == MAX_PINS;
        this.spare = !this.strike && nbrPins1 + nbrPins2 == MAX_PINS;
    }

    /**
     * Zero frame condition check
     *
     * @return True if no pins have been knocked down after all possible attempts have been made. Otherwise, returns false.
     */
    public final boolean isZero() {
        return zero;
    }

    /**
     * Open frame condition check
     *
     * @return True if at least one pin is left standing after all possible attempts have been made. Otherwise, returns false.
     */
    public final boolean isOpen() {
        return open;
    }

    /**
     * Spare condition check
     *
     * @return True if all pins have been knocked down on the first two attempts. Otherwise, returns false.
     */
    public final boolean isSpare() {
        return spare;
    }

    /**
     * Strike condition check
     *
     * @return True if all pins have been knocked down on the first attempt. Otherwise, returns false.
     */
    public boolean isStrike() {
        return strike;
    }

    /**
     * Score condition check
     *
     * @return True if this frame has already been scored. Otherwise, returns false.
     */
    public final boolean hasScore() {
        return score > -1;
    }

    /**
     * Get score
     *
     * @return The score for this frame or -1 if it hasn't been scored yet.
     */
    public final int score() {
        return score;
    }

    /**
     * Settle the score :)
     *
     * @param start The starting number of points
     * @param bonus The number of bonus points
     */
    public final void updateScore(int start, int bonus) {
        if (start < 0)
            throw new IllegalArgumentException("Starting points cannot be negative");
        if (bonus < 0)
            throw new IllegalArgumentException("Bonus points cannot be negative");
        synchronized (lock) {
            this.score = start + total() + bonus;
        }
    }

    /**
     * Get first roll
     *
     * @return The number of pins knocked down in the first roll
     */
    public final int firstRoll() {
        return firstRoll;
    }

    /**
     * Get second roll
     *
     * @return The number of pins knocked down in the second roll
     */
    public final int secondRoll() {
        return secondRoll;
    }

    /**
     * Get total
     *
     * @return The total number of pins knocked down in this frame
     */
    public int total() {
        return firstRoll + secondRoll;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(firstRoll) + Integer.hashCode(secondRoll);
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
        return this.firstRoll == that.firstRoll && this.secondRoll == that.secondRoll;
    }

    @Override
    public String toString() {
        return String.format("%s [firstRoll=%d, secondRoll=%d, zero=%b, open=%b, spare=%b, strike=%b, score=%s]",
                getClass().getSimpleName(), firstRoll, secondRoll, zero, open, spare, strike, score);
    }
}

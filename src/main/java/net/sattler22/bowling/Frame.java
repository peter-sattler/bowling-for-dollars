package net.sattler22.bowling;

import net.jcip.annotations.ThreadSafe;

import java.util.OptionalInt;

/**
 * A Ten Pin Bowling <code>Frame</code> represents a series of turns or opportunities for
 * a bowler to throw the ball down the lane and attempt to knock down the pins.
 *
 * @author Pete Sattler
 * @since July 2025
 * @version August 2025
 */
@ThreadSafe
abstract sealed class Frame permits DefaultFrame, FinalFrame {

    /**
     * Maximum pins per frame
     */
    static final int MAX_PINS = 10;

    /**
     * Maximum rolls per frame
     */
    static final int MAX_ROLLS = 2;

    /**
     * Maximum score per game
     */
    static final int MAX_SCORE = 300;

    protected final int attempt1;
    protected final int attempt2;
    protected volatile int score = -1;
    private final Object lock = new Object();

    /**
     * Constructs a new <code>Frame</code>
     *
     * @param attempt1 The number of pins knocked down in the first attempt
     * @param attempt2 The number of pins knocked down in the second attempt
     */
    protected Frame(int attempt1, int attempt2) {
        if (attempt1 < 0 || attempt2 < 0)
            throw new IllegalArgumentException("Invalid number of pins");
        if (attempt1 > MAX_PINS || attempt2 > MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins exceeded");
        this.attempt1 = attempt1;
        this.attempt2 = attempt2;
    }

    /**
     * Open frame condition check
     *
     * @return True if at least one pin is left standing after all possible
     *         attempts have been made. Otherwise, returns false.
     */
    boolean isOpen() {
        return total() < MAX_PINS;
    }

    /**
     * Zero frame condition check
     *
     * @return True if no pins have been knocked down after all possible
     *         attempts have been made. Otherwise, returns false.
     */
    final boolean isZero() {
        return total() == 0;
    }

    /**
     * Score condition check
     *
     * @return True if this frame has already been scored. Otherwise, returns false.
     */
    boolean hasScore() {
        return score > -1;
    }

    /**
     * Get score
     *
     * @return A score for this frame or an empty optional if it hasn't been scored yet.
     */
    OptionalInt score() {
        if (!hasScore())
            return OptionalInt.empty();
        return OptionalInt.of(score);
    }

    /**
     * Settle the score :)
     *
     * @param start The starting number of points
     * @param bonus The number of bonus points to add to this frame's total score
     */
    void updateScore(int start, int bonus) {
        if (start < 0)
            throw new IllegalArgumentException("Starting points cannot be negative");
        if (start > MAX_SCORE)
            throw new IllegalArgumentException("Starting points cannot exceed the maximum");
        if (bonus < 0)
            throw new IllegalArgumentException("Bonus points cannot be negative");
        if (bonus > MAX_PINS)
            throw new IllegalArgumentException("Bonus points cannot exceed the maximum");
        synchronized (lock) {
            this.score = start + total() + bonus;
        }
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
     * @return The total number of pins knocked down in this frame
     */
    int total() {
        return attempt1 + attempt2;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(attempt1) + Integer.hashCode(attempt2);
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
        return this.attempt1 == that.attempt1 && this.attempt2 == that.attempt2;
    }

    @Override
    public String toString() {
        return String.format("%s [attempt1=%d, attempt2=%d, score=%s]",
                getClass().getSimpleName(), attempt1, attempt2, hasScore() ? score : "None");
    }
}

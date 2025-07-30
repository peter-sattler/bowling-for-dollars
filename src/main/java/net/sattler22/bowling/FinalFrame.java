package net.sattler22.bowling;

import net.jcip.annotations.ThreadSafe;

/**
 * A Ten Pin Bowling <code>FinalFrame</code> has up to three consecutive rolls (attempts) and represents
 * the last frame in the game. The third attempt is allowed only if all pins have been knocked down on the
 * first two attempts.
 *
 * @author Pete Sattler
 * @version July 2025
 */
@ThreadSafe
final class FinalFrame extends Frame {

    /**
     * Maximum rolls per frame (including bonus roll)
     */
    static final int MAX_ROLLS_WITH_BONUS = MAX_ROLLS + 1;

    private final int attempt3;

    /**
     * Constructs a new <code>FinalFrame</code>
     *
     * @param nbrPins1 The number of pins knocked down in the first attempt
     * @param nbrPins2 The number of pins knocked down in the second attempt
     * @param nbrPins3 The number of pins knocked down in the third (bonus) attempt
     */
    FinalFrame(int nbrPins1, int nbrPins2, int nbrPins3) {
        super(nbrPins1, nbrPins2);
        if (nbrPins1 > MAX_PINS || nbrPins2 > MAX_PINS || nbrPins3 > MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins exceeded");
        if (nbrPins1 != Frame.MAX_PINS && nbrPins1 + nbrPins2 != Frame.MAX_PINS && nbrPins3 > 0)
            throw new IllegalArgumentException("Bonus roll has not been earned");
        this.attempt3 = nbrPins3;
    }

    /**
     * Turkey condition check
     *
     * @return True if all pins have been knocked down in all three frames. Otherwise, returns false.
     */
    boolean isTurkey() {
        return attempt1 == MAX_PINS && attempt2 == MAX_PINS && attempt3 == MAX_PINS;
    }

    /**
     * Get third attempt
     *
     * @return The number of pins knocked down in the third (bonus) attempt
     */
    int thirdAttempt() {
        return attempt3;
    }

    @Override
    int total() {
        return super.total() + attempt3;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + Integer.hashCode(attempt3);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (this.getClass() != other.getClass())
            return false;
        final FinalFrame that = (FinalFrame) other;
        return super.equals(other) && this.attempt3 == that.attempt3;
    }

    @Override
    public String toString() {
        return String.format("%s [attempt1=%s, attempt2=%s, attempt3=%s]",
                getClass().getSimpleName(), attempt1, attempt2, attempt3);
    }
}

package net.sattler22.bowling.model;

import net.jcip.annotations.ThreadSafe;

/**
 * A Ten Pin Bowling <code>FinalFrame</code> has up to three consecutive rolls (attempts) and represents
 * the last frame in the game. The bonus (third) attempt is allowed only if all pins have been knocked down
 * on the first two attempts.
 *
 * @author Pete Sattler
 * @since July 2025
 * @version August 2025
 */
@ThreadSafe
public final class FinalFrame extends Frame {

    /**
     * Maximum rolls per frame (including bonus roll)
     */
    public static final int MAX_ROLLS_WITH_BONUS = MAX_ROLLS + 1;

    private final int bonusAttempt;

    /**
     * Constructs a new <code>FinalFrame</code>
     *
     * @param nbrPins1 The number of pins knocked down in the first attempt
     * @param nbrPins2 The number of pins knocked down in the second attempt
     * @param bonusNbrPins The number of pins knocked down in the bonus (third) attempt
     */
    public FinalFrame(int nbrPins1, int nbrPins2, int bonusNbrPins) {
        super(nbrPins1, nbrPins2);
        if (nbrPins1 > MAX_PINS || nbrPins2 > MAX_PINS || bonusNbrPins > MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins exceeded");
        if (nbrPins1 != Frame.MAX_PINS && nbrPins1 + nbrPins2 != Frame.MAX_PINS && bonusNbrPins > 0)
            throw new IllegalArgumentException("Bonus roll has not been earned");
        this.bonusAttempt = bonusNbrPins;
    }

    /**
     * Turkey condition check
     *
     * @return True if all pins have been knocked down in all three frames. Otherwise, returns false.
     */
    public boolean isTurkey() {
        return attempt1 == MAX_PINS && attempt2 == MAX_PINS && bonusAttempt == MAX_PINS;
    }

    /**
     * Spare condition check
     *
     * @return True if all pins have been knocked down on the first two attempts. Otherwise, returns false.
     */
    public boolean isSpare() {
        return attempt1 != MAX_PINS && super.total() == MAX_PINS;
    }

    /**
     * Get bonus attempt
     *
     * @return The number of pins knocked down in the bonus (third) attempt
     */
    public int bonusAttempt() {
        return bonusAttempt;
    }

    @Override
    public int total() {
        return super.total() + bonusAttempt;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + Integer.hashCode(bonusAttempt);
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
        return super.equals(other) && this.bonusAttempt == that.bonusAttempt;
    }

    @Override
    public String toString() {
        return String.format("%s [attempt1=%s, attempt2=%s, bonusAttempt=%s]",
                getClass().getSimpleName(), attempt1, attempt2, bonusAttempt);
    }
}

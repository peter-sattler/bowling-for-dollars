package net.sattler22.bowling.model;

import net.jcip.annotations.ThreadSafe;

/**
 * A Ten Pin Bowling {@code FinalFrame} has up to three consecutive rolls and represents the last frame in the
 * game. The bonus (third) roll is allowed if all pins have been knocked down on the first two attempts.
 *
 * @author Pete Sattler
 * @since July 2025
 * @version June 2026
 */
@ThreadSafe
public final class FinalFrame extends Frame {

    private final int bonusRoll;
    private final boolean turkey;

    /**
     * Constructs a new {@code FinalFrame}
     *
     * @param nbrPins1 The number of pins knocked down in the first roll
     * @param nbrPins2 The number of pins knocked down in the second roll
     */
    public FinalFrame(int nbrPins1, int nbrPins2) {
        this(nbrPins1, nbrPins2, 0);
    }

    /**
     * Constructs a new {@code FinalFrame}
     *
     * @param nbrPins1 The number of pins knocked down in the first roll
     * @param nbrPins2 The number of pins knocked down in the second roll
     * @param bonusNbrPins The number of pins knocked down in the bonus (third) roll
     */
    public FinalFrame(int nbrPins1, int nbrPins2, int bonusNbrPins) {
        if (bonusNbrPins < 0)
            throw new IllegalArgumentException("Invalid number of bonus pins");
        if (bonusNbrPins > MAX_PINS || (nbrPins1 == MAX_PINS && nbrPins2 != MAX_PINS && nbrPins2 + bonusNbrPins > MAX_PINS))
            throw new IllegalArgumentException("Maximum number of bonus pins exceeded");
        if (bonusNbrPins > 0 && !hasEarnedBonusRoll(nbrPins1, nbrPins2))
            throw new IllegalArgumentException("Bonus roll has not been earned");
        super(nbrPins1, nbrPins2);
        this.bonusRoll = bonusNbrPins;
        this.turkey = nbrPins1 == MAX_PINS && nbrPins2 == MAX_PINS && bonusNbrPins == MAX_PINS;
    }

    /**
     * Copy constructs a new {@code FinalFrame}
     *
     * @param source The source frame
     */
    FinalFrame(FinalFrame source) {
        this(source.firstRoll, source.secondRoll, source.bonusRoll);
        synchronized (lock) {
            this.score = source.score;
        }
    }

    /**
     * Bonus roll earned condition check
     *
     * @param nbrPins1 The number of pins knocked down in the first roll
     * @param nbrPins2 The number of pins knocked down in the second roll
     * @return True if all pins have been knocked down on the first two rolls. Otherwise, returns false.
     */
    public static boolean hasEarnedBonusRoll(int nbrPins1, int nbrPins2) {
        return nbrPins1 == Frame.MAX_PINS || nbrPins1 + nbrPins2 == Frame.MAX_PINS;
    }

    /**
     * Turkey condition check
     *
     * @return True if all pins have been knocked down in all three frames. Otherwise, returns false.
     */
    public boolean isTurkey() {
        return turkey;
    }

    /**
     * Update the score
     *
     * @param start The starting number of points
     */
    public void updateScore(int start) {
        if (start < 0)
            throw new IllegalArgumentException("Starting points cannot be negative");
        if (hasScore())
            throw new IllegalStateException("Score has already been updated");
        synchronized (lock) {
            this.score = start + total();
        }
    }

    /**
     * Get bonus attempt
     *
     * @return The number of pins knocked down in the bonus (third) attempt
     */
    public int bonusRoll() {
        return bonusRoll;
    }

    @Override
    public int total() {
        return super.total() + bonusRoll;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + Integer.hashCode(bonusRoll);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof FinalFrame that))
            return false;
        return super.equals(other) && this.bonusRoll == that.bonusRoll;
    }

    @Override
    public String toString() {
        return String.format("%s [firstRoll=%d, secondRoll=%d, bonusRoll=%d, zero=%b, open=%b, spare=%b, turkey=%b, score=%s]",
                getClass().getSimpleName(), firstRoll, secondRoll, bonusRoll, isZero(), isOpen(), isSpare(), turkey, score);
    }
}

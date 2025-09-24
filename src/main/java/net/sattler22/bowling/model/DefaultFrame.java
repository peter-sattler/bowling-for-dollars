package net.sattler22.bowling.model;

import net.jcip.annotations.ThreadSafe;

/**
 * A Ten Pin Bowling <code>DefaultFrame</code> allows up to two consecutive rolls and represents all frames in a game,
 * except for the final frame which includes a possible bonus roll.
 *
 * @author Pete Sattler
 * @since July 2025
 * @version October 2025
 */
@ThreadSafe
public final class DefaultFrame extends Frame {

    private final boolean strike;

    /**
     * Constructs a new <code>DefaultFrame</code>
     *
     * @param nbrPins1 The number of pins knocked down in the first roll
     * @param nbrPins2 The number of pins knocked down in the second roll
     */
    public DefaultFrame(int nbrPins1, int nbrPins2) {
        super(nbrPins1, nbrPins2);
        if (nbrPins1 + nbrPins2 > MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins exceeded");
        this.strike = nbrPins1 == MAX_PINS;
    }

    /**
     * Record a strike
     *
     * @return A new <code>DefaultFrame</code>
     */
    public static DefaultFrame strike() {
        return new DefaultFrame(MAX_PINS, 0);
    }

    /**
     * Strike condition check
     *
     * @return True if all pins have been knocked down on the first attempt. Otherwise, returns false.
     */
    public boolean isStrike() {
        return strike;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public String toString() {
        return String.format("%s [firstRoll=%d, secondRoll=%d, zero=%b, open=%b, spare=%b, strike=%b, score=%s]",
                getClass().getSimpleName(), firstRoll(), secondRoll(), isZero(), isOpen(), isSpare(), strike, score());
    }
}

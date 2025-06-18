package net.sattler22.bowling;

import net.jcip.annotations.Immutable;

/**
 * A <code>DefaultFrame</code> represents up to two consecutive rolls (attempts) in a ten pin bowling game, except for
 * the <code>FinalFrame</code> which has up to three consecutive rolls.
 *
 * @author Pete Sattler
 * @since June 2025
 * @version June 2025
 */
@Immutable
final class DefaultFrame extends Frame {

    /**
     * Constructs a new <code>DefaultFrame</code>
     *
     * @param attempt1 The number of pins knocked down in the first attempt
     * @param attempt2 The number of pins knocked down in the second attempt
     */
    private DefaultFrame(int attempt1, int attempt2) {
        super(attempt1, attempt2);
        if (attempt1 + attempt2 > MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins exceeded");
    }

    /**
     * Record a strike
     *
     * @return A new <code>Frame</code>
     */
    static Frame strike() {
        return new DefaultFrame(MAX_PINS, 0);
    }

    /**
     * Strike condition check
     *
     * @return True if all pins have been knocked down on the first attempt. Otherwise, returns false.
     */
    boolean isStrike() {
        return attempt1 == MAX_PINS;
    }

    /**
     * Record a spare
     *
     * @return A new <code>Frame</code>
     */
    static Frame spare(int nbrPins1) {
        if (nbrPins1 == MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins for a spare exceeded");
        return new DefaultFrame(nbrPins1, MAX_PINS - nbrPins1);
    }

    /**
     * Spare condition check
     *
     * @return True if all pins have been knocked down on the both attempts. Otherwise, returns false.
     */
    boolean isSpare() {
        return !isStrike() && total() == MAX_PINS;
    }

    /**
     * Record an open frame
     *
     * @return A new <code>Frame</code>
     */
    static Frame open(int nbrPins1, int nbrPins2) {
        if (nbrPins1 + nbrPins2 == MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins for an open frame exceeded");
        return new DefaultFrame(nbrPins1, nbrPins2);
    }

    /**
     * Record a zero frame
     *
     * @return A new <code>Frame</code>
     */
    static Frame zero() {
        return new DefaultFrame(0, 0);
    }
}

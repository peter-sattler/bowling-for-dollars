package net.sattler22.bowling;

import net.jcip.annotations.ThreadSafe;

/**
 * A Ten Pin Bowling <code>DefaultFrame</code> has up to two consecutive rolls (attempts) and
 * represents most frames in a ten pin bowling game, except for the last one (which as up to
 * three consecutive rolls).
 *
 * @author Pete Sattler
 * @version July 2025
 */
@ThreadSafe
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
     * Strike condition check
     *
     * @return True if all pins have been knocked down on the first attempt. Otherwise, returns false.
     */
    boolean isStrike() {
        return attempt1 == MAX_PINS;
    }

    /**
     * Record a strike
     *
     * @return A new <code>DefaultFrame</code>
     */
    static DefaultFrame strike() {
        return new DefaultFrame(MAX_PINS, 0);
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
     * Record a spare
     *
     * @return A new <code>DefaultFrame</code>
     */
    static DefaultFrame spare(int nbrPins1) {
        if (nbrPins1 == MAX_PINS)
            throw new IllegalArgumentException("Maximum number of first attempt pins for a spare exceeded");
        return new DefaultFrame(nbrPins1, MAX_PINS - nbrPins1);
    }

    /**
     * Record an open frame
     *
     * @return A new <code>DefaultFrame</code>
     */
    static DefaultFrame open(int nbrPins1, int nbrPins2) {
        if (nbrPins1 + nbrPins2 == MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins for an open frame exceeded");
        return new DefaultFrame(nbrPins1, nbrPins2);
    }

    /**
     * Record a zero (gutter ball) frame
     *
     * @return A new <code>DefaultFrame</code>
     */
    static DefaultFrame zero() {
        return new DefaultFrame(0, 0);
    }
}

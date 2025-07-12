package net.sattler22.bowling;

import net.jcip.annotations.Immutable;

/**
 * A Ten Pin Bowling <code>DefaultFrame</code> has up to two consecutive rolls (attempts) and
 * represents most frames in the game, except for the last one (which as up to three consecutive rolls).
 *
 * @author Pete Sattler
 * @version July 2025
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
     * Strike condition check
     *
     * @return True if all pins have been knocked down on the first attempt. Otherwise, returns false.
     */
    boolean isStrike() {
        return attempt1 == MAX_PINS;
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
     * Record a strike
     *
     * @return A new <code>DefaultFrame</code>
     */
    static DefaultFrame strike() {
        return new DefaultFrame(MAX_PINS, 0);
    }

    /**
     * Record a non-strike frame
     *
     * @return A new <code>DefaultFrame</code>
     */
    static DefaultFrame nonStrike(int nbrPins1, int nbrPins2) {
        if (nbrPins1 == MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins for a non-strike exceeded");
        if (nbrPins1 + nbrPins2 > MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins exceeded");
        return new DefaultFrame(nbrPins1, nbrPins2);
    }
}

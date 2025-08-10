package net.sattler22.bowling.model;

import net.jcip.annotations.ThreadSafe;

import java.util.List;

/**
 * A Ten Pin Bowling <code>DefaultFrame</code> has up to two consecutive rolls (attempts) and
 * represents most frames in the game, except for the last one (which as up to three consecutive rolls).
 *
 * @author Pete Sattler
 * @since July 2025
 * @version August 2025
 */
@ThreadSafe
public final class DefaultFrame extends Frame {

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
     * @return A new <code>DefaultFrame</code>
     */
    public static DefaultFrame strike() {
        return new DefaultFrame(MAX_PINS, 0);
    }

    /**
     * Record a non-strike frame
     *
     * @return A new <code>DefaultFrame</code>
     */
    public static DefaultFrame nonStrike(int nbrPins1, int nbrPins2) {
        if (nbrPins1 == MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins for a non-strike exceeded");
        if (nbrPins1 + nbrPins2 > MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins exceeded");
        return new DefaultFrame(nbrPins1, nbrPins2);
    }

    /**
     * Get roll total
     *
     * @param frames One or more default frames
     * @param nbrRolls The number of rolls
     * @return The total number of pins knocked down in the number of requested rolls
     */
    public static int rollTotal(List<DefaultFrame> frames, int nbrRolls) {
        if (frames == null)
            throw new IllegalArgumentException("Frames is required");
        if (nbrRolls < 0)
            throw new IllegalArgumentException("Number of rolls cannot be negative");
        int sum = 0;
        int count = 0;
        for(final DefaultFrame frame : frames) {
            if (count < nbrRolls) {
                sum += frame.firstAttempt();
                count++;
            }
            //Bypass non-rolls which are ZERO after a STRIKE:
            if (frame.firstAttempt() < Frame.MAX_PINS && count < nbrRolls) {
                sum += frame.secondAttempt();
                count++;
            }
            if (count == nbrRolls)
                break;
        }
        return sum;
    }

    /**
     * Strike condition check
     *
     * @return True if all pins have been knocked down on the first attempt. Otherwise, returns false.
     */
    public boolean isStrike() {
        return attempt1 == MAX_PINS;
    }

    /**
     * Spare condition check
     *
     * @return True if all pins have been knocked down on the both attempts. Otherwise, returns false.
     */
    public boolean isSpare() {
        return !isStrike() && total() == MAX_PINS;
    }
}

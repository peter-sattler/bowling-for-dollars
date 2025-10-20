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
    }

    /**
     * Record a strike
     *
     * @return A new <code>DefaultFrame</code>
     */
    public static DefaultFrame strike() {
        return new DefaultFrame(MAX_PINS, 0);
    }

    DefaultFrame(DefaultFrame source) {
        this(source.firstRoll(), source.secondRoll());
    }

    /**
     * Update the score
     *
     * @param start The starting number of points
     * @param bonus The number of bonus points
     */
    public void updateScore(int start, int bonus) {
        if (start < 0)
            throw new IllegalArgumentException("Starting points cannot be negative");
        if (bonus < 0)
            throw new IllegalArgumentException("Bonus points cannot be negative");
        if (hasScore())
            throw new IllegalStateException("Score has already been updated");
        synchronized (lock) {
            this.score = start + total() + bonus;
        }
    }
}

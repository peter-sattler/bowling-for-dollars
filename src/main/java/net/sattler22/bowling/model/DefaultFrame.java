package net.sattler22.bowling.model;

import net.jcip.annotations.ThreadSafe;

/**
 * A Ten Pin Bowling {@code DefaultFrame} allows up to two consecutive rolls and represents all frames in a game,
 * except for the final frame which includes a possible bonus roll.
 *
 * @author Pete Sattler
 * @since July 2025
 * @version June 2026
 */
@ThreadSafe
public final class DefaultFrame extends Frame {

    /**
     * Constructs a new {@code DefaultFrame}
     *
     * @param nbrPins1 The number of pins knocked down in the first roll
     * @param nbrPins2 The number of pins knocked down in the second roll
     */
    public DefaultFrame(int nbrPins1, int nbrPins2) {
        if (nbrPins1 == MAX_PINS && nbrPins2 != 0)
            throw new IllegalArgumentException("Maximum number of pins exceeded");
        super(nbrPins1, nbrPins2);
    }

    /**
     * Record a strike
     *
     * @return A new {@code DefaultFrame}
     */
    public static DefaultFrame strike() {
        return new DefaultFrame(MAX_PINS, 0);
    }

    /**
     * Copy constructs a new {@code DefaultFrame}
     *
     * @param source The source frame
     */
    DefaultFrame(DefaultFrame source) {
        this(source.firstRoll, source.secondRoll);
        synchronized (source.lock) {
            this.score = source.score;
        }
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
        synchronized (lock) {
            if (hasScore())
                throw new IllegalStateException("Score has already been updated");
            this.score = start + super.total() + bonus;
        }
    }
}

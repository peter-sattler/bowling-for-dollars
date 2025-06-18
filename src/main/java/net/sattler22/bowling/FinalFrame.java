package net.sattler22.bowling;

import net.jcip.annotations.Immutable;

/**
 * A <code>FinalFrame</code> represents up to three consecutive rolls (attempts) in the last frame of a ten pin
 * bowling game.
 *
 * @author Pete Sattler
 * @since June 2025
 * @version June 2025
 */
@Immutable
final class FinalFrame extends Frame {

    private final int attempt3;

    private FinalFrame(int attempt1, int attempt2, int attempt3) {
        super(attempt1, attempt2);
        if (attempt1 > MAX_PINS || attempt2 > MAX_PINS || attempt3 > MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins exceeded");
        this.attempt3 = attempt3;
    }

    /**
     * Record three strikes in a row
     *
     * @return A new <code>Frame</code>
     */
    static Frame allStrikes() {
        return new FinalFrame(MAX_PINS, MAX_PINS, MAX_PINS);
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
     * Record a strike, then a spare
     *
     * @return A new <code>Frame</code>
     */
    static Frame strikeThenSpare(int nbrPins2) {
        if (nbrPins2 == MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins for a spare exceeded");
        return new FinalFrame(MAX_PINS, nbrPins2, MAX_PINS - nbrPins2);
    }

    /**
     * Record a strike, then an open frame
     *
     * @return A new <code>Frame</code>
     */
    static Frame strikeThenOpen(int nbrPins2, int nbrPins3) {
        if (nbrPins2 + nbrPins3 == MAX_PINS)
            throw new IllegalArgumentException("An open frame means at least one pin was left standing");
        return new FinalFrame(MAX_PINS, nbrPins2, nbrPins3);
    }

    /**
     * Record a spare, then a strike
     *
     * @return A new <code>Frame</code>
     */
    static Frame spareThenStrike(int nbrPins1) {
        if (nbrPins1 == MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins for a spare exceeded");
        return new FinalFrame(nbrPins1, MAX_PINS - nbrPins1, MAX_PINS);
    }

    /**
     * Record a spare, then an open frame
     *
     * @return A new <code>Frame</code>
     */
    static Frame spareThenOpen(int nbrPins1, int nbrPins3) {
        if (nbrPins1 == MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins for a spare exceeded");
        if (nbrPins3 == MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins for an open frame exceeded");
        return new FinalFrame(nbrPins1, MAX_PINS - nbrPins1, nbrPins3);
    }

    /**
     * Record an open frame
     *
     * @return A new <code>Frame</code>
     */
    static Frame open(int nbrPins1, int nbrPins2) {
        if (nbrPins1 + nbrPins2 == MAX_PINS)
            throw new IllegalArgumentException("An open frame means at least one pin was left standing");
        return new FinalFrame(nbrPins1, nbrPins2, 0);
    }

    /**
     * Record a zero frame
     *
     * @return A new <code>Frame</code>
     */
    static Frame zero() {
        return new FinalFrame(0, 0, 0);
    }

    /**
     * Get third attempt
     *
     * @return The number of pins knocked down in the third attempt
     */
    int thirdAttempt() {
        return attempt3;
    }

    @Override
    int total() {
        return super.total() + attempt3;
    }

    @Override
    public String toString() {
        return String.format("%s [number=%d, attempt1=%s, attempt2=%s, attempt3=%s]",
                getClass().getSimpleName(), number, attempt1, attempt2, attempt3);
    }
}

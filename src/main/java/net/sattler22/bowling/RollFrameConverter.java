package net.sattler22.bowling;

import net.jcip.annotations.ThreadSafe;

import java.util.Optional;

/**
 * Ten Pin Bowling Roll to Frame Converter
 * <p>
 * Record each roll and convert them to frames. No scoring is performed. Assumes that all zero
 * pin rolls have been captured.
 * </p>
 *
 * @author Pete Sattler
 * @version August 2025
 */
@ThreadSafe
final class RollFrameConverter {

    private final RollTracker rollTracker = new RollTracker();

    /**
     * Record a roll
     *
     * @param nbrPins The number of pins for this roll
     */
    void roll(int nbrPins) {
        if (nbrPins < 0)
            throw new IllegalArgumentException("Invalid number of pins");
        if (nbrPins > Frame.MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins exceeded");
        rollTracker.add(nbrPins);
    }

    /**
     * Convert individual rolls into a frame
     *
     * @param isFinal False for a {@link DefaultFrame} or true for a {@link FinalFrame}
     * @return An optional {@link Frame}
     */
    Optional<Frame> convert(boolean isFinal) {
        return Optional.ofNullable(!isFinal ? convertToDefaultFrameImpl() : convertToFinalFrameImpl());
    }

    private DefaultFrame convertToDefaultFrameImpl() {
        if (rollTracker.nextRollIsStrike()) {
            rollTracker.getNext();
            return DefaultFrame.strike();
        }
        if (rollTracker.size() == Frame.MAX_ROLLS)
            return DefaultFrame.nonStrike(rollTracker.getNext(), rollTracker.getNext());
        return null;
    }

    private FinalFrame convertToFinalFrameImpl() {
        if (!hasEarnedBonusRoll() && rollTracker.size() == Frame.MAX_ROLLS)
            return new FinalFrame(rollTracker.getNext(), rollTracker.getNext(), 0);
        //Bonus for first STRIKE or first SPARE:
        if (hasEarnedBonusRoll() && rollTracker.size() == FinalFrame.MAX_ROLLS_WITH_BONUS)
            return new FinalFrame(rollTracker.getNext(), rollTracker.getNext(), rollTracker.getNext());
        return null;
    }

    private boolean hasEarnedBonusRoll() {
        return rollTracker.nextRollIsStrike() || rollTracker.nextTwoRollsIsSpare();
    }

    /**
     * Get size
     *
     * @return The number of rolls
     */
    int size() {
        return rollTracker.size();
    }

    /**
     * Get total
     *
     * @return The total number of pins for all rolls
     */
    int total() {
        return rollTracker.total();
    }

    @Override
    public String toString() {
        return String.format("%s [rollTracker=%s]", getClass().getSimpleName(), rollTracker);
    }
}

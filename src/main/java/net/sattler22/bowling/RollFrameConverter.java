package net.sattler22.bowling;

import java.util.Optional;

/**
 * Ten Pin Bowling Roll to Frame Converter
 * <p>
 * Record each roll and convert them to frames. No scoring is performed. Assumes that zero
 * pin rolls have been captured.
 * </p>
 *
 * @author Pete Sattler
 * @version July 2025
 */
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
     * Convert individual rolls into a default frame
     *
     * @return An optional {@link DefaultFrame}
     */
    Optional<DefaultFrame> convertToDefaultFrame() {
        if (rollTracker.nextRollIsStrike()) {
            rollTracker.getNext();
            return Optional.of(DefaultFrame.strike());
        }
        if (rollTracker.size() == Frame.MAX_ROLLS)
            return Optional.of(DefaultFrame.nonStrike(rollTracker.getNext(), rollTracker.getNext()));
        return Optional.empty();
    }

    /**
     * Convert individual rolls into a final frame
     * <p>
     * A bonus roll is earned in the final frame:
     * <ol>
     * <li>If a player bowls a strike, then they get two more throws.</li>
     * <li>If a player bowls a spare, then they get one more throw.</li>
     * </ol>
     * </p>
     * @return An optional {@link FinalFrame}
     */
    Optional<FinalFrame> convertToFinalFrame() {
        if (!hasEarnedBonusRoll() && rollTracker.size() == Frame.MAX_ROLLS)
            return Optional.of(new FinalFrame(rollTracker.getNext(), rollTracker.getNext(), 0));
        if (hasEarnedBonusRoll() && rollTracker.size() == FinalFrame.MAX_ROLLS_WITH_BONUS)
            return Optional.of(new FinalFrame(rollTracker.getNext(), rollTracker.getNext(), rollTracker.getNext()));
        return Optional.empty();
    }

    private boolean hasEarnedBonusRoll() {
        return rollTracker.nextRollIsStrike() || rollTracker.nextTwoRollsIsSpare();
    }

    /**
     * Get total
     *
     * @return The total number of pins for all rolls
     */
    int total() {
        return rollTracker.total();
    }

    /**
     * Get size
     *
     * @return The number of rolls
     */
    int size() {
        return rollTracker.size();
    }

    @Override
    public String toString() {
        return String.format("%s [rollTracker=%s]", getClass().getSimpleName(), rollTracker);
    }
}

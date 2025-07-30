package net.sattler22.bowling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

import static net.sattler22.bowling.FrameList.FrameIterator;

/**
 * Ten Pin Bowling {@link Game}
 *
 * <p>
 * Tracks and scores all frames for a ten pin bowling player.
 * </p>
 * <p>
 * Rules:
 * <ol>
 * <li>The ultimate goal is to knock down all ten pins on your first turn.</li>
 * <li>During each frame, each player gets two attempts to knock down all ten pins. Turns are called frames,
 * and each player plays ten frames in a game.</li>
 * <li>Knocking down all the pins on your first throw is called a strike.</li>
 * <li>If you miss at least one pin on the first throw and then knock down any remaining pins on your second
 * throw, it's called a spare.</li>
 * <li>Open frames are simply frames that left at least one pin standing.</li>
 * <li>Scoring is based on the number of pins knocked down. Except, when you get a spare, you get 10 plus the
 * number of pins you knock down during your next throw. If you get a strike, you get 10 plus the number of pins
 * you knock down with your next two throws.</li>
 * If a player bowls a strike in the tenth (final) frame, they get two more throws within that frame. If they get a
 * spare in the final frame, the player gets to throw one more ball.</li>
 * <li>Honor the foul line. If a player steps over the foul line or crosses it in any way, those pins will not count
 * toward that player's score</li>
 * </ol>
 * </p>
 *
 * @author Pete Sattler
 * @version August 2025
 */
final class Game {

    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    private final String playerName;
    private final RollFrameConverter rollFrameConverter = new RollFrameConverter();
    private final FrameList frameList = new FrameList();

    /**
     * Constructs a new <code>Game</code>
     *
     * @param playerName The player's name
     */
    Game(String playerName) {
        if (playerName == null || playerName.isBlank())
            throw new IllegalArgumentException("Player name is required");
        this.playerName = playerName;
    }

    /**
     * Get player name
     *
     * @return The player associated with this game
     */
    String playerName() {
        return playerName;
    }

    /**
     * Game over condition check
     *
     * @return True if the game is over (all frames recorded). Otherwise, returns false.
     */
    boolean isOver() {
        return frameList.isFull();
    }

    /**
     * Record a strike
     */
    void strike() {
        roll(Frame.MAX_PINS);
    }

    /**
     * Record a roll
     *
     * @param nbrPins The number of pins for this roll
     */
    void roll(int nbrPins) {
        if (isOver())
            throw new IllegalStateException("%s's game is over".formatted(playerName));
        rollFrameConverter.roll(nbrPins);
        if (nbrPins == Frame.MAX_PINS)
            logger.info("{} rolled a STRIKE!!!", playerName);
        else
            logger.info("{} knocked down {} pin{}", playerName, nbrPins, nbrPins == 1 ? "" : "s");
        final Frame frame =
                rollFrameConverter.convert(frameList.isFinalFrame())
                        .orElse(null);
        if (frame != null) {
            frameList.add(frame);
            for(final Frame updatedFrame : updateScore())
                logger.info("{}'s rolls converted to {}", playerName, updatedFrame);
        }
    }

    private List<Frame> updateScore() {
        final List<Frame> updatedFrames = new ArrayList<>();
        for(final FrameIterator frameIterator = frameList.iterator(); frameIterator.hasNext();) {
            final Frame currentFrame = frameIterator.next();
            if (!currentFrame.hasScore()) {
                final int bonus = calculateBonus(currentFrame, frameIterator).orElse(-1);
                if (bonus > -1) {
                    currentFrame.updateScore(score(), bonus);
                    updatedFrames.add(currentFrame);
                }
            }
        }
        return updatedFrames;
    }

    private static OptionalInt calculateBonus(Frame currentFrame, FrameIterator frameIterator) {
        return switch (currentFrame) {
            case DefaultFrame currentDefaultFrame -> calculateBonus(currentDefaultFrame, frameIterator);
            case FinalFrame currentFinalFrame -> calculateBonus(currentFinalFrame, frameIterator);
        };
    }

    //Scoring is based on the number of pins knocked down, except:
    // -When you get a SPARE, you get 10 plus the number of pins you knock down during your next throw.
    // -When you get a STRIKE, you get 10 plus the number of pins you knock down with your next two throws.
    private static OptionalInt calculateBonus(DefaultFrame currentFrame, FrameIterator frameIterator) {
        if (currentFrame.isOpen())
            return OptionalInt.of(0);
        if (frameIterator.hasNext()) {
            final DefaultFrame peekNextFrame = (DefaultFrame) frameIterator.peekNext();
            if (currentFrame.isSpare())
                return OptionalInt.of(peekNextFrame.attempt1);
            if (currentFrame.isStrike())
                return OptionalInt.of(peekNextFrame.total());
        }
        return OptionalInt.empty();
    }

    private static OptionalInt calculateBonus(FinalFrame currentFrame, FrameIterator frameIterator) {
        return OptionalInt.of(0);
    }

    /**
     * Score a single player's game
     */
    int score() {
        return frameList.stream()
                .filter(Frame::hasScore)
                .map(Frame::score)
                .flatMapToInt(OptionalInt::stream)
                .reduce((first, second) -> second)
                .orElse(0);
    }

    /**
     * Get size
     *
     * @return The number of frames bowled so far
     */
    int size() {
        return frameList.size();
    }

    @Override
    public String toString() {
        return String.format("%s [playerName=%s, rollFrameConverter=%s]",
                getClass().getSimpleName(), playerName, rollFrameConverter);
    }
}

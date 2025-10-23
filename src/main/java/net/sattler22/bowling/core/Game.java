package net.sattler22.bowling.core;

import net.jcip.annotations.ThreadSafe;
import net.sattler22.bowling.model.DefaultFrame;
import net.sattler22.bowling.model.FinalFrame;
import net.sattler22.bowling.model.Frame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;

/**
 * Ten Pin Bowling Game
 *
 * <p>
 * Tracks and scores all frames for a ten pin bowling player.
 * </p>
 * <p>
 * Scoring Rules:
 * <ol>
 * <li>The ultimate goal is to knock down all ten pins on your first turn.</li>
 * <li>During each frame, a player gets two attempts to knock down all ten pins. Each player plays ten frames in a
 * game.</li>
 * <li>Knocking down all ten pins on your first throw is called a strike.</li>
 * <li>If you miss at least one pin on the first throw and then knock down all remaining pins on your second throw, it's
 * called a spare.</li>
 * <li>Open frames are simply frames that leave at least one pin standing.</li>
 * <li>Scoring is based on the number of pins knocked down. Except, when you get a spare, you get ten plus the
 * number of pins you knock down during your next throw. If you get a strike, you get ten plus the number of pins you
 * knock down on your next two throws.</li>
 * <li>If a player bowls a strike in the tenth (final) frame, they get two more throws within that frame. If they get a
 * spare in the final frame, the player gets to throw one more ball.</li>
 * <li>Honor the foul line. If a player steps over the foul line or crosses it in any way, those pins will not count
 * toward that player's score</li>
 * </ol>
 * </p>
 *
 * @author Pete Sattler
 * @version October 2025
 */
@ThreadSafe
public final class Game {

    /**
     * Maximum frames allowed
     */
    public static final int MAX_FRAMES = 10;

    private final String playerName;
    private final List<Frame> frames = Collections.synchronizedList(new ArrayList<>());

    /**
     * Constructs a new <code>Game</code>
     *
     * @param playerName The player's name
     */
    public Game(String playerName) {
        if (playerName == null || playerName.isBlank())
            throw new IllegalArgumentException("Player name is required");
        this.playerName = playerName;
    }

    /**
     * Get player name
     *
     * @return The player associated with this game
     */
    public String playerName() {
        return playerName;
    }

    /**
     * Game over condition check
     *
     * @return True if the game is over (all frames recorded). Otherwise, returns false.
     */
    public boolean isOver() {
        return frames.size() == MAX_FRAMES;
    }

    /**
     * Perfect game condition check
     *
     * @return True if all pins in the game were knocked over. Otherwise, returns false.
     */
    public boolean isPerfect() {
        return score() == 300;
    }

    /**
     * Add a frame
     *
     * @param frame The new {@link Frame}
     */
    public void addFrame(Frame frame) {
        if (isOver())
            throw new IllegalStateException("%s's game is over".formatted(playerName));
        if (frame == null)
            throw new IllegalArgumentException("Frame is required");
        if (frames.size() == MAX_FRAMES - 1 && !(frame instanceof FinalFrame))
            throw new IllegalArgumentException("Final frame is required");
        frames.add(Frame.copyOf(frame));  //Defensive copy
    }

    /**
     * Update score
     *
     * @return Zero or more {@link Frame}s that were updated
     */
    public List<Frame> updateScore() {
        final List<Frame> updatedFrames = new ArrayList<>();
        for (int index = 0; index < frames.size(); index++) {
            final Frame currentFrame = frames.get(index);
            if (!currentFrame.hasScore()) {
                final int start = index > 0 ? frames.get(index - 1).score().orElse(0) : 0;
                switch(currentFrame) {
                    case DefaultFrame defaultFrame -> {
                        int bonus = calculateBonus(defaultFrame, index);
                        if (bonus > -1) {
                            defaultFrame.updateScore(start, bonus);
                            updatedFrames.add(currentFrame);
                        }
                    }
                    case FinalFrame finalFrame -> {
                        finalFrame.updateScore(start);
                        updatedFrames.add(currentFrame);
                    }
                }
            }
        }
        return updatedFrames;
    }

    private int calculateBonus(DefaultFrame defaultFrame, final int index) {
        //No BONUS:
        if (defaultFrame.isZero() || defaultFrame.isOpen())
            return 0;
        //SPARE bonus is next roll:
        if (defaultFrame.isSpare() && index < frames.size() - 1)
            return frames.get(index + 1).firstRoll();
        //STRIKE bonus is next two rolls (over a ONE frame):
        if (index < frames.size() - 1) {
            final Frame nextFrame = frames.get(index + 1);
            if (nextFrame.isOpen() || nextFrame.isSpare() || nextFrame instanceof FinalFrame)
                return nextFrame.firstRoll() + nextFrame.secondRoll();
        }
        //STRIKE bonus is next two rolls (over TWO frames):
        if (index < frames.size() - 2 && frames.get(index + 1).isStrike())
            return frames.get(index + 1).firstRoll() + frames.get(index + 2).firstRoll();
        return -1;
    }

    /**
     * Score a single player's game
     */
    public int score() {
        return frames.reversed().stream()
                .filter(Frame::hasScore)
                .map(Frame::score)
                .flatMapToInt(OptionalInt::stream)
                .findFirst()
                .orElse(0);
    }

    @Override
    public String toString() {
        return String.format("%s [playerName=%s, frames=%s]", getClass().getSimpleName(), playerName, frames);
    }
}

package net.sattler22.bowling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ten pin <code>Game</code> represents all rolls (attempts) for a single player
 *
 * @author Pete Sattler
 * @since June 2025
 * @version June 2025
 */
final class Game {

    /**
     * Maximum number of frames in a <code>Game</code>
     */
    public static final int MAX_FRAMES = 10;

    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    private final String playerName;
    private final RollFrameConverter rollFrameConverter;

    Game(String playerName) {
        if (playerName == null || playerName.isBlank())
            throw new IllegalArgumentException("Player name is required");
        this.playerName = playerName;
        this.rollFrameConverter = new RollFrameConverter();
    }

    void roll(int nbrPins) {
        rollFrameConverter.roll(nbrPins);
    }

    /**
     * Calculate the score
     *
     * @return The final score
     */
    int score() {
        //Score as much as possible:
        int points = 0;
        //Score keeper takes one or more frames
        return points;
    }

    /**
     * Get player name
     *
     * @return The player associated with this <code>Game</code>
     */
    String playerName() {
        return playerName;
    }

    @Override
    public String toString() {
        return String.format("%s [playerName=%s, rollFrameConverter=%s]", getClass().getSimpleName(), playerName, rollFrameConverter);
    }
}

package net.sattler22.bowling;

import net.sattler22.bowling.model.DefaultFrame;
import net.sattler22.bowling.model.FinalFrame;
import net.sattler22.bowling.model.Frame;
import net.sattler22.bowling.model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Ten Pin Bowling Score Calculator
 * <p>
 * A simple command-line scoring calculator for the amazing Ten Pin Bowling game
 * </p>
 *
 * @author Pete Sattler
 * @version October 2025
 */
public final class ScoreCalculator {

    private static final Logger logger = LoggerFactory.getLogger(ScoreCalculator.class);
    private static final String USER_TERMINATE = "quit";

    public static void main(String[] args) {
        logger.info("*** Ten Pin Bowling Score Calculator ***");
        try(final Scanner scanner = new Scanner(System.in)) {
            logger.info("Enter player name or quit to terminate: ");
            final String playerName = scanner.nextLine();
            if (USER_TERMINATE.equalsIgnoreCase(playerName))
                logger.info("Ten Pin Bowling game terminated");
            else {
                final Game game = new Game(playerName);
                for (int index = 0; index < Game.MAX_FRAMES - 1; index++) {
                    final DefaultFrame defaultFrame;
                    final int nbrPins1 = captureRoll(scanner, "FIRST", index + 1);
                    if (nbrPins1 == Frame.MAX_PINS)
                        defaultFrame = DefaultFrame.strike();
                    else {
                        final int nbrPins2 = captureRoll(scanner, "SECOND", index + 1);
                        defaultFrame = new DefaultFrame(nbrPins1, nbrPins2);
                    }
                    game.addFrame(defaultFrame);
                    for(final Frame updatedFrame : game.calculateScore())
                        logger.info("Added frame for {}: {}", playerName, updatedFrame);
                }
                final int nbrPins1 = captureRoll(scanner, "FIRST", Frame.MAX_PINS);
                final int nbrPins2 = captureRoll(scanner, "SECOND", Frame.MAX_PINS);
                final int bonusNbrPins;
                if (FinalFrame.hasEarnedBonusRoll(nbrPins1, nbrPins2))
                    bonusNbrPins = captureRoll(scanner, "BONUS", Frame.MAX_PINS);
                else
                    bonusNbrPins = 0;
                game.addFrame(new FinalFrame(nbrPins1, nbrPins2, bonusNbrPins));
                game.calculateScore();
                if (game.isOver()) {
                    logger.info("{}'s total score: {}", playerName, game.score());
                    if (game.isPerfect())
                        logger.info("Congratulations, you have bowled a PERFECT game!!!");
                }
                logger.info("Ten Pin Bowling game complete");
            }
        }
        catch (RuntimeException exception) {
            logger.error(exception.getMessage(), exception);
            System.exit(1);
        }
        System.exit(0);
    }

    private static int captureRoll(Scanner scanner, String attemptWord, int frameNbr) {
        logger.info("Enter pins knocked down for {} attempt of frame #{}: ", attemptWord, frameNbr);
        final int nbrPins = scanner.nextInt();
        if (nbrPins < 0)
            throw new IllegalArgumentException("Invalid number of pins");
        if (nbrPins > Frame.MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins exceeded");
        return nbrPins;
    }
}

package net.sattler22.bowling.client;

import net.sattler22.bowling.core.Game;
import net.sattler22.bowling.model.DefaultFrame;
import net.sattler22.bowling.model.FinalFrame;
import net.sattler22.bowling.model.Frame;
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

    /**
     * Command-line Ten Pin Bowling Scoring Calculator
     *
     * @param args Not supported
     */
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
                    game.addFrame(captureDefaultFrame(scanner, index + 1));
                    for(final Frame updatedFrame : game.updateScore())
                        logger.info("Added frame for {}: {}", playerName, updatedFrame);
                }
                final FinalFrame finalFrame = captureFinalFrame(scanner);
                game.addFrame(finalFrame);
                game.updateScore();
                if (finalFrame.isTurkey())
                    logger.info("Nice, a TURKEY on the final frame!!!");
                logger.info("{}'s total score: {}", playerName, game.score());
                if (game.isPerfect())
                    logger.info("Congratulations, you have bowled a PERFECT game!!!");
                logger.info("Ten Pin Bowling game complete");
            }
        }
        catch (RuntimeException exception) {
            logger.error(exception.getMessage(), exception);
            System.exit(1);
        }
        System.exit(0);
    }

    private static DefaultFrame captureDefaultFrame(Scanner scanner, int frameNbr) {
        final int nbrPins1 = captureRoll(scanner, "FIRST", frameNbr);
        if (nbrPins1 == Frame.MAX_PINS)
            return DefaultFrame.strike();
        final int nbrPins2 = captureRoll(scanner, "SECOND", frameNbr);
        return new DefaultFrame(nbrPins1, nbrPins2);
    }

    private static FinalFrame captureFinalFrame(Scanner scanner) {
        final int nbrPins1 = captureRoll(scanner, "FIRST", Frame.MAX_PINS);
        final int nbrPins2 = captureRoll(scanner, "SECOND", Frame.MAX_PINS);
        if (FinalFrame.hasEarnedBonusRoll(nbrPins1, nbrPins2)) {
            final int bonusNbrPins = captureRoll(scanner, "BONUS", Frame.MAX_PINS);
            return new FinalFrame(nbrPins1, nbrPins2, bonusNbrPins);
        }
        return new FinalFrame(nbrPins1, nbrPins2);
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

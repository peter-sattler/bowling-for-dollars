package net.sattler22.bowling.client;

import net.sattler22.bowling.core.Game;
import net.sattler22.bowling.model.DefaultFrame;
import net.sattler22.bowling.model.FinalFrame;
import net.sattler22.bowling.model.Frame;

import java.io.Console;

/**
 * Ten Pin Bowling Score Calculator
 * <p>
 * A simple command-line scoring calculator for the amazing Ten Pin Bowling game
 * </p>
 *
 * @author Pete Sattler
 * @since October 2025
 * @version June 2026
 */
public final class ScoreCalculator {

    private static final String USER_TERMINATE = "quit";

    /**
     * Executes the Ten Pin Bowling Scoring Calculator
     */
    static void main() {
        final Console console = System.console();
        if (console == null || !console.isTerminal()) {
            System.err.println("Please run from a terminal/command prompt");
            return;
        }
        console.printf("*** Ten Pin Bowling Score Calculator ***%n");
        console.printf("Enter player name or quit to terminate: ");
        final String playerName = console.readLine();
        if (USER_TERMINATE.equalsIgnoreCase(playerName)) {
            console.printf("Ten Pin Bowling game terminated%n");
            return;
        }
        try {
            final Game game = new Game(playerName);
            for (int index = 0; index < Game.MAX_FRAMES - 1; index++) {
                game.addFrame(captureDefaultFrame(console, index + 1));
                for (final Frame updatedFrame : game.updateScore())
                    console.printf("Added frame for %s: %s%n", playerName, updatedFrame);
            }
            final FinalFrame finalFrame = captureFinalFrame(console);
            game.addFrame(finalFrame);
            game.updateScore();
            if (finalFrame.isTurkey())
                console.printf("Nice, a TURKEY on the final frame!!!%n");
            console.printf("%s's total score: %s%n", playerName, game.score());
            if (game.isPerfect())
                console.printf("Congratulations, you have bowled a PERFECT game!!!%n");
            console.printf("Ten Pin Bowling game complete%n");
        }
        catch (RuntimeException runtimeException) {
            console.printf("INPUT ERROR: %s!!!%n", runtimeException.getMessage());
        }
    }

    private static DefaultFrame captureDefaultFrame(Console console, int frameNbr) {
        final int nbrPins1 = captureRoll(console, "FIRST", frameNbr);
        if (nbrPins1 == Frame.MAX_PINS)
            return DefaultFrame.strike();
        final int nbrPins2 = captureRoll(console, "SECOND", frameNbr);
        return new DefaultFrame(nbrPins1, nbrPins2);
    }

    private static FinalFrame captureFinalFrame(Console console) {
        final int nbrPins1 = captureRoll(console, "FIRST", Game.MAX_FRAMES);
        final int nbrPins2 = captureRoll(console, "SECOND", Game.MAX_FRAMES);
        if (FinalFrame.hasEarnedBonusRoll(nbrPins1, nbrPins2)) {
            final int bonusNbrPins = captureRoll(console, "BONUS", Game.MAX_FRAMES);
            return new FinalFrame(nbrPins1, nbrPins2, bonusNbrPins);
        }
        return new FinalFrame(nbrPins1, nbrPins2);
    }

    private static int captureRoll(Console console, String attemptWord, int frameNbr) {
        final String userInput =
            console.readLine("Enter pins knocked down for %s attempt of frame #%d: ", attemptWord, frameNbr);
        final int nbrPins = Integer.parseInt(userInput);
        if (nbrPins < 0)
            throw new IllegalArgumentException("Invalid number of pins");
        if (nbrPins > Frame.MAX_PINS)
            throw new IllegalArgumentException("Maximum number of pins exceeded");
        return nbrPins;
    }
}

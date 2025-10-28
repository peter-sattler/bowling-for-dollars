package net.sattler22.bowling.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ten Pin Bowling Default Frame Unit Tests
 *
 * @author Pete Sattler
 * @since July 2025
 * @version October 2025
 */
@DisplayName("Default Frame Unit Tests")
final class DefaultFrameTest {

    @Nested
    @DisplayName("Constructs a New Frame")
    final class NewInstanceTest {
        @Test
        void newInstance_withTooFewPins1_thenThrowIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () ->
                    new DefaultFrame(-1, 2)
            );
        }

        @Test
        void newInstance_withTooFewPins2_thenThrowIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () ->
                    new DefaultFrame(1, -1)
            );
        }

        @Test
        void newInstance_withTooManyPins1_thenThrowIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () ->
                    new DefaultFrame(Frame.MAX_PINS + 1, 2)
            );
        }

        @Test
        void newInstance_withTooManyPins2_thenThrowIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () ->
                    new DefaultFrame(1, Frame.MAX_PINS + 1)
            );
        }

        @Test
        void newInstance_withTooManyTotalPins_thenThrowIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () ->
                    new DefaultFrame(5, 6)
            );
        }

        @Test
        void newInstance_withHappyPath_thenSuccessful() {
            final int nbrPins1 = 3;
            final int nbrPins2 = 4;
            final DefaultFrame openFrame = new DefaultFrame(nbrPins1, nbrPins2);
            assertEquals(nbrPins1, openFrame.firstRoll());
            assertEquals(nbrPins2, openFrame.secondRoll());
        }

        @Test
        void strike_withHappyPath_thenSuccessful() {
            final DefaultFrame strikeFrame = DefaultFrame.strike();
            assertEquals(Frame.MAX_PINS, strikeFrame.firstRoll());
            assertEquals(0, strikeFrame.secondRoll());
        }
    }

    @Nested
    @DisplayName("Copy an Existing Frame")
    final class CopyInstanceTest {
        @Test
        void copyInstance_withHappyPath_thenSuccessful() {
            final Frame frame = spareFrame(4);
            final Frame frameCopy = Frame.copyOf(frame);
            assertInstanceOf(DefaultFrame.class, frameCopy);
            assertEquals(frame, frameCopy);
            assertNotSame(frameCopy, frame);
        }
    }

    @Nested
    @DisplayName("Zero Frame Condition Check")
    final class IsZeroTest {
        @Test
        void isZero_withTooManyPins_thenReturnFalse() {
            assertFalse(new DefaultFrame(1, 0).isZero());
        }

        @Test
        void isZero_withHappyPath_thenReturnTrue() {
            assertTrue(zeroFrame().isZero());
        }
    }

    @Nested
    @DisplayName("Open Frame Condition Check")
    final class IsOpenTest {
        @Test
        void isOpen_withZeroPins_thenReturnTrue() {
            assertTrue(zeroFrame().isOpen());
        }

        @Test
        void isOpen_withHappyPath_thenReturnTrue() {
            assertTrue(new DefaultFrame(1, 6).isOpen());
        }

        @Test
        void isOpen_withSpare_thenReturnFalse() {
            assertFalse(spareFrame(1).isOpen());
        }

        @Test
        void isOpen_withStrike_thenReturnFalse() {
            assertFalse(DefaultFrame.strike().isOpen());
        }
    }

    @Nested
    @DisplayName("Spare Condition Check")
    final class IsSpareTest {
        @Test
        void isSpare_withZeroPins_thenReturnFalse() {
            assertFalse(zeroFrame().isSpare());
        }

        @Test
        void isSpare_withTooFewPins_thenReturnFalse() {
            assertFalse(new DefaultFrame(2, 6).isSpare());
        }

        @Test
        void isSpare_withHappyPath_thenReturnTrue() {
            assertTrue(spareFrame(5).isSpare());
        }

        @Test
        void isSpare_withStrike_thenReturnFalse() {
            assertFalse(DefaultFrame.strike().isSpare());
        }
    }

    @Nested
    @DisplayName("Strike Condition Check")
    final class IsStrikeTest {
        @Test
        void isStrike_withZeroPins_thenReturnFalse() {
            assertFalse(zeroFrame().isStrike());
        }

        @Test
        void isStrike_withTooFewPins_thenReturnFalse() {
            assertFalse(new DefaultFrame(4, 4).isStrike());
        }

        @Test
        void isStrike_withSpare_thenReturnFalse() {
            assertFalse(spareFrame(9).isStrike());
        }

        @Test
        void isStrike_withHappyPath_thenReturnTrue() {
            assertTrue(DefaultFrame.strike().isStrike());
        }
    }

    @Nested
    @DisplayName("Score Condition Check")
    final class HasScoreTest {
        @Test
        void hasScore_withoutUpdate_thenReturnFalse() {
            assertFalse(zeroFrame().hasScore());
        }

        @Test
        void hasScore_withHappyPath_thenReturnTrue() {
            final DefaultFrame zeroFrame = zeroFrame();
            zeroFrame.updateScore(0, 0);
            assertTrue(zeroFrame.hasScore());
        }
    }

    @Nested
    @DisplayName("Update the Score")
    final class UpdateScoreTest {
        @Test
        void updateScore_withNegativeStartingPoints_thenThrowIllegalArgumentException() {
            final DefaultFrame zeroFrame = zeroFrame();
            assertThrows(IllegalArgumentException.class, () ->
                    zeroFrame.updateScore(-1, 0));
        }

        @Test
        void updateScore_withNegativeBonusPoints_thenThrowIllegalArgumentException() {
            final DefaultFrame zeroFrame = zeroFrame();
            assertThrows(IllegalArgumentException.class, () ->
                    zeroFrame.updateScore(0, -1));
        }

        @Test
        void updateScore_withExistingScore_thenThrowIllegalStateException() {
            final DefaultFrame zeroFrame = zeroFrame();
            zeroFrame.updateScore(0, 0);
            assertThrows(IllegalStateException.class, () ->
                    zeroFrame.updateScore(0, 0));
        }

        @Test
        void updateScore_withHappyPath_thenSuccessful() {
            final int nbrPins1 = 6;
            final int nbrPins2 = 3;
            final int start = 25;
            final int bonus = 10;
            final DefaultFrame openFrame = new DefaultFrame(nbrPins1, nbrPins2);
            openFrame.updateScore(start, bonus);
            assertEquals(nbrPins1 + nbrPins2 + start + bonus, openFrame.score().orElse(-1));
        }
    }

    @Nested
    @DisplayName("Get Score")
    final class ScoreTest {
        @Test
        void score_withoutUpdate_thenReturnEmpty() {
            assertTrue(zeroFrame().score().isEmpty());
        }

        @Test
        void score_withHappyPath_thenReturnTrue() {
            final DefaultFrame zeroFrame = zeroFrame();
            zeroFrame.updateScore(0, 0);
            assertTrue(zeroFrame.score().isPresent());
            assertEquals(0, zeroFrame.score().orElse(-1));
        }
    }

    @Nested
    @DisplayName("Get Total")
    final class TotalTest {
        @Test
        void total_withHappyPath_thenSuccessful() {
            final int nbrPins1 = 7;
            final int nbrPins2 = 2;
            final DefaultFrame defaultFrame = new DefaultFrame(nbrPins1, nbrPins2);
            assertEquals(nbrPins1 + nbrPins2, defaultFrame.total());
        }
    }

    @Nested
    @DisplayName("Equality Check")
    final class EqualsTest {
        @Test
        void equals_whenSame_thenReturnTrue() {
            final DefaultFrame openFrame1 = new DefaultFrame(2, 0);
            final DefaultFrame openFrame2 = new DefaultFrame(2, 0);
            assertEquals(openFrame1, openFrame2);
        }

        @Test
        void equals_whenDifferent_thenReturnFalse() {
            final DefaultFrame openFrame1 = new DefaultFrame(1, 1);
            final DefaultFrame openFrame2 = new DefaultFrame(1, 2);
            assertNotEquals(openFrame1, openFrame2);
        }
    }

    private static DefaultFrame zeroFrame() {
        return new DefaultFrame(0, 0);
    }

    private static DefaultFrame spareFrame(int nbrPins1) {
        return new DefaultFrame(nbrPins1, Frame.MAX_PINS - nbrPins1);
    }
}

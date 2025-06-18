package net.sattler22.bowling;

import org.junit.jupiter.api.Test;

/**
 * Single <code>Game</code> Test Harness
 *
 * -Start with game, then Scorecard
 *
 * @author Pete Sattler
 * @since June 2025
 * @version June 2025
 */
final class GameTest {
  
  @Test
  void testIsOpen() {
    final Game peteGame = new Game("Pete");
    peteGame.roll(8);
    peteGame.roll(1);
  }
}

package net.sattler22.bowling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Ten Pin Bowling Roll to Frame Converter
 * <p>
 * Record each roll and convert them to frames. No scoring is performed. 
 * </p>
 *
 * @author Pete Sattler
 * @since June 2025
 * @version June 2025
 */
public final class RollFrameConverter {
  
  private static final Logger logger = LoggerFactory.getLogger(RollFrameConverter.class);
  private static final int MAX_FRAMES = 10;
  private final RollTracker rollTracker = new RollTracker();
  private final List<Frame> frames = new ArrayList<>(MAX_FRAMES);
  
  boolean hasFrames() {
    return frames.size() < MAX_FRAMES;
  }
  
  void roll(int nbrPins) {
    if (!hasFrames())
      throw new IllegalStateException("No more rolls allowed");
    if (rollTracker.total() + nbrPins > Frame.MAX_PINS)
      throw new IllegalArgumentException("Maximum number of pins exceeded");
    rollTracker.add(nbrPins);
    convertImpl();
  }
  
  boolean isFinalFrame() {
    return frames.size() == MAX_FRAMES - 1;
  }
  
  //Convert rolls into a frame:
  private void convertImpl() {
    //TODO: Process final frame
    if (isFinalFrame()) {
      return;
    }
    
    //TODO: Process default frame
    //TODO: Have enough rolls for a frame???
    //
    

    //TODO: OLD CODE
    //if (rolls.size() == 1 && rolls.getFirst().nbrPins() == RollList.MAX_PINS) {
      //frames.add(DefaultFrame.strike());
    //}
    
    //TODO: Update list of frames
    
    //rollList.markLastConverted();
  }
}

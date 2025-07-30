package net.sattler22.bowling;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * Ten Pin Bowling Peekable {@link Frame} List
 *
 * @author Pete Sattler
 * @version August 2025
 */
final class FrameList {

    static final int MAX_FRAMES = 10;
    private final List<Frame> frames = new LinkedList<>();

    /**
     * Add a new frame
     *
     * @param frame The new frame to add
     */
    void add(Frame frame) {
        frames.add(frame);
    }

    /**
     * Get size
     *
     * @return The number of frames
     */
    int size() {
        return frames.size();
    }

    /**
     * Final frame condition check
     *
     * @return True if the next frame add will be the last one permitted. Otherwise, returns false.
     */
    boolean isFinalFrame() {
        return frames.size() == MAX_FRAMES - 1;
    }

    /**
     * Maximum frames condition check
     *
     * @return True if no more frames are allowed. Otherwise, returns false.
     */
    boolean isFull() {
        return frames.size() == MAX_FRAMES;
    }

    public Stream<Frame> stream() {
        return frames.stream();
    }

    /**
     * Get frame iterator
     *
     * @return A peekable, frame iterator
     */
    public FrameIterator iterator() {
        return new FrameIterator(frames.listIterator());
    }

    /**
     * Peekable frame iterator
     */
    static final class FrameIterator implements ListIterator<Frame> {

        private final ListIterator<Frame> source;

        /**
         * Constructs a new, peekable frame iterator
         *
         * @param source The source iterator
         */
        private FrameIterator(ListIterator<Frame> source) {
            this.source = source;
        }

        @Override
        public void add(Frame frame) {
            source.add(frame);
        }

        @Override
        public boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public Frame next() {
            return source.next();
        }

        @Override
        public int nextIndex() {
            return source.nextIndex();
        }

        /**
         * Returns the next element in the list, but maintains the cursor
         * position. This method should not be used to iterate through the list.
         *
         * @return The next element in the list
         * @throws NoSuchElementException When the iteration has no next element
         */
        public Frame peekNext() {
            final Frame next = source.next();
            source.previous();  //Restore position
            return next;
        }

        @Override
        public boolean hasPrevious() {
            return source.hasPrevious();
        }

        @Override
        public Frame previous() {
            return source.previous();
        }

        @Override
        public int previousIndex() {
            return source.previousIndex();
        }

        @Override
        public void set(Frame frame) {
            throw new UnsupportedOperationException("Frame replacement is not allowed");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Frame removal is not allowed");
        }
    }
}

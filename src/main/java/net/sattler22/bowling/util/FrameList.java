package net.sattler22.bowling.util;

import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;
import net.sattler22.bowling.model.Frame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;

/**
 * Ten Pin Bowling peek-ahead {@link Frame} List
 *
 * @author Pete Sattler
 * @version August 2025
 */
@ThreadSafe
public final class FrameList {

    /**
     * Maximum frames allowed
     */
    public static final int MAX_FRAMES = 10;

    private final List<Frame> frames = Collections.synchronizedList(new ArrayList<>());

    /**
     * Add a new frame
     *
     * @param frame The new frame to add
     */
    public void add(Frame frame) {
        frames.add(frame);
    }

    /**
     * Get all frames
     *
     * @return A copy of all frames in the list
     */
    public List<Frame> frames() {
        return List.copyOf(frames);
    }

    /**
     * Get size
     *
     * @return The number of frames
     */
    public int size() {
        return frames.size();
    }

    /**
     * Final frame condition check
     *
     * @return True if the next frame add will be the last one permitted. Otherwise, returns false.
     */
    public boolean isFinalFrame() {
        return frames.size() == MAX_FRAMES - 1;
    }

    /**
     * Maximum frames condition check
     *
     * @return True if no more frames are allowed. Otherwise, returns false.
     */
    public boolean isFull() {
        return frames.size() == MAX_FRAMES;
    }

    /**
     * Get frame stream
     *
     * @return A sequential {@code Stream} of all frames
     */
    public Stream<Frame> stream() {
        return frames.stream();
    }

    /**
     * Get frame iterator
     *
     * @return A peek-ahead, frame iterator
     */
    public FrameIterator iterator() {
        return new FrameIterator(frames.listIterator());
    }

    /**
     * Peek-ahead frame iterator
     * <p>
     * This iterator is not thread-safe and will fail fast throwing a {@link java.util.ConcurrentModificationException}
     * </p>
     */
    @NotThreadSafe
    public static final class FrameIterator implements ListIterator<Frame> {

        private final ListIterator<Frame> source;

        /**
         * Constructs a new, peek-ahead frame iterator
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
         * Returns all next elements in the list, but maintains the cursor
         * position. This method should not be used to iterate through the list.
         *
         * @return All ot the next elements in the list (or an empty list when no elements are found)
         */
        public List<Frame> peekNextAll() {
            final List<Frame> nextList = new ArrayList<>();
            int count = 0;
            while (source.hasNext()) {
                nextList.add(source.next());
                count++;
            }
            for(int i = 0; i < count; i++)
                source.previous();  //Restore position
            return nextList;
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

package ws.spring.util;

import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Function;

/**
 * @author WindShadow
 * @version 2025-05-28.
 */
public final class SegmentStreams {

    public static <E> SegmentStream<E> emptySegmentStream(int interval) {
        return new EmptySegmentStream<>(interval);
    }

    public static <E> SegmentStream<E> ofCollection(int interval, Collection<E> collection) {

        if (Objects.requireNonNull(collection).isEmpty()) return emptySegmentStream(interval);
        List<E> elements = new ArrayList<>(collection);
        return ofSelector(interval, (offset, s) -> {

            if (offset >= elements.size()) return Collections.emptyList();
            int toIndex = Math.min(offset + s, elements.size());
            return elements.subList(offset, toIndex);
        });
    }

    public static <E> SegmentStream<E> ofArray(int interval, E[] array) {

        return Objects.requireNonNull(array).length == 0 ?
                emptySegmentStream(interval) : ofCollection(interval, Arrays.asList(array));
    }

    public static <E> SegmentStream<E> ofSelector(int interval, SegmentVernier<E> vernier) {

        return new SegmentStreamImpl<>(interval, Function.identity(), vernier);
    }

    private static class EmptySegmentStream<E> extends AbstractSegmentStream<E> {

        private EmptySegmentStream(int interval) {
            super(interval);
        }

        @Override
        public boolean isClosed() {
            return true;
        }

        @Override
        public void reset() {
        }

        @Override
        public <T> SegmentStream<T> map(int interval, Function<? super E, ? extends T> mapping) {
            return new EmptySegmentStream<>(interval);
        }

        @Override
        public List<E> nextSegment() {
            return Collections.emptyList();
        }

        @Override
        public Iterator<E> iterator() {
            return Collections.emptyIterator();
        }

        @Override
        public void close() throws Exception {
        }
    }

    private static class SegmentStreamImpl<S, E> extends AbstractSegmentStream<E> {

        private static final Queue<?> EMPTY_SEGMENT = new LinkedList<>();

        private int offset;
        private final Function<? super S, ? extends E> mapping;
        private final SegmentVernier<S> vernier;

        private Queue<S> segment;

        private boolean closed;

        private SegmentStreamImpl(int interval, Function<? super S, ? extends E> mapping, SegmentVernier<S> vernier) {
            super(interval);
            this.mapping = Objects.requireNonNull(mapping, "The mapping must not be null");
            this.vernier = Objects.requireNonNull(vernier, "The vernier must not be null");
            this.reset();
        }

        private Queue<S> nextSourceSegment() {

            Assert.state(!isClosed(), "The SegmentStream is closed");
            if (segment.isEmpty()) {

                List<S> sourceSegment;
                try {
                    sourceSegment = this.vernier.peek(this.offset, this.interval);
                    Assert.state(sourceSegment != null, "The segment of source is null");
                } catch (Throwable e) {
                    doClose();
                    throw e;
                }
                if (sourceSegment.isEmpty()) {
                    doClose();
                } else {
                    this.segment = new LinkedList<>(sourceSegment);
                    this.offset += segment.size();
                }
            }
            return segment;
        }

        private void doClose() {
            this.closed = true;
        }

        private void checkState() {
            Assert.state(!isClosed(), "The SegmentStream is closed");
        }

        @Override
        public void close() throws Exception {

            checkState();
            doClose();
        }

        @Override
        public boolean isClosed() {
            return this.closed;
        }

        @Override
        public void reset() {

            this.offset = 0;
            this.closed = false;
            this.segment = (Queue<S>) EMPTY_SEGMENT;
        }

        @Override
        public <T> SegmentStream<T> map(int interval, Function<? super E, ? extends T> mapping) {

            Objects.requireNonNull(mapping, "The mapping must not be null");
            Function<? super S, ? extends T> useMapping = s -> mapping.apply(this.mapping.apply(s));
            SegmentStreamImpl<S, T> stream = new SegmentStreamImpl<>(interval, useMapping, this.vernier);
            stream.offset = this.offset;
            stream.closed = this.closed;
            stream.segment = this.segment;
            this.segment = (Queue<S>) EMPTY_SEGMENT;
            this.doClose();
            return stream;
        }

        @Override
        public List<E> nextSegment() {

            checkState();
            Queue<S> sourceSegment = nextSourceSegment();
            if (sourceSegment.isEmpty()) return Collections.emptyList();
            List<E> els = new ArrayList<>();
            while (!sourceSegment.isEmpty()) {
                els.add(mapping.apply(sourceSegment.poll()));
            }
            return Collections.unmodifiableList(els);
        }

        @Override
        public Iterator<E> iterator() {

            checkState();
            return new SegmentStreamIterator<>(this);
        }
    }

    private static class SegmentStreamIterator<S, E> implements Iterator<E> {

        private final SegmentStreamImpl<S, E> stream;

        private SegmentStreamIterator(SegmentStreamImpl<S, E> stream) {
            this.stream = stream;
        }

        @Override
        public boolean hasNext() {
            return !stream.isClosed() && !stream.nextSourceSegment().isEmpty();
        }

        @Override
        public E next() {

            stream.checkState();
            return stream.mapping.apply(stream.nextSourceSegment().poll());
        }
    }
}

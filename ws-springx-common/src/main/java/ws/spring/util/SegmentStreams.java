package ws.spring.util;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public static <E> SegmentStream<E> ofSelector(int interval, SegmentStream.SegmentVernier<E> vernier) {

        return new SegmentStreamImpl<>(interval, Function.identity(), vernier);
    }

    private static class EmptySegmentStream<E> extends AbstractSegmentStream<E> {

        private EmptySegmentStream(int interval) {
            super(interval);
        }

        @Override
        public void reset() {
        }

        @Override
        public <T> SegmentStream<T> map(int interval, Function<? super E, ? extends T> mapping) {
            return new EmptySegmentStream<>(interval);
        }

        @Override
        public int getOffset() {
            return 0;
        }

        @Override
        public List<E> nextSegment() {
            return Collections.emptyList();
        }

        @Override
        public void forEach(Predicate<E> predicate) {
        }

        @Override
        public void forEachSegment(Predicate<List<E>> predicate) {
        }
    }

    private static class SegmentStreamImpl<S, E> extends AbstractSegmentStream<E> {

        private int offset;
        private final Function<? super S, ? extends E> mapping;
        private final SegmentVernier<S> vernier;

        private boolean ultimate;

        private SegmentStreamImpl(int interval, Function<? super S, ? extends E> mapping, SegmentVernier<S> vernier) {
            super(interval);
            this.mapping = Objects.requireNonNull(mapping, "The mapping must not be null");
            this.vernier = Objects.requireNonNull(vernier, "The vernier must not be null");
            this.reset();
        }

        @Override
        public void reset() {

            this.offset = 0;
            this.ultimate = false;
        }

        @Override
        public <T> SegmentStream<T> map(int interval, Function<? super E, ? extends T> mapping) {

            Objects.requireNonNull(mapping, "The mapping must not be null");
            Function<? super S, ? extends T> useMapping = s -> mapping.apply(this.mapping.apply(s));
            SegmentStreamImpl<S, T> stream = new SegmentStreamImpl<>(interval, useMapping, this.vernier);
            stream.offset = this.offset;
            stream.ultimate = this.ultimate;
            return stream;
        }

        @Override
        public int getOffset() {
            return this.offset;
        }

        @Override
        public List<E> nextSegment() {

            List<S> segment = nextSourceSegment();
            return segment.isEmpty() ? Collections.emptyList() : segment.stream().map(this.mapping).collect(Collectors.toList());
        }

        @Override
        public void forEach(Predicate<E> predicate) {

            Objects.requireNonNull(predicate);
            List<S> segment;
            while (!this.ultimate && !(segment = nextSourceSegment()).isEmpty()) {
                for (S s : segment) {
                    if (!predicate.test(this.mapping.apply(s))) return;
                }
            }
        }

        @Override
        public void forEachSegment(Predicate<List<E>> predicate) {

            Objects.requireNonNull(predicate);
            List<E> segment;
            while (!this.ultimate && !(segment = nextSegment()).isEmpty()) if (!predicate.test(segment)) return;
        }

        private List<S> nextSourceSegment() {

            if (this.ultimate) return Collections.emptyList();
            List<S> es = this.vernier.peek(this.offset, this.interval);
            int size = es.size();
            this.ultimate = size < this.interval;
            this.offset += size;
            return es;
        }
    }
}

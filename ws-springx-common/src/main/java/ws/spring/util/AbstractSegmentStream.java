package ws.spring.util;

public abstract class AbstractSegmentStream<E> implements SegmentStream<E> {

    protected final int interval;

    public AbstractSegmentStream(int interval) {

        if (interval <= 0) {
            throw new IllegalArgumentException("Invalid interval: " + interval);
        }
        this.interval = interval;
    }

    @Override
    public final int getInterval() {
        return interval;
    }
}
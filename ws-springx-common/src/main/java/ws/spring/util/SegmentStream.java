package ws.spring.util;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 分段流
 *
 * @author WindShadow
 * @version 2025-05-28.
 * @see SegmentStreams
 */
public interface SegmentStream<E> extends AutoCloseable, Iterable<E> {

    boolean isClosed();

    /**
     * 重置此分段流
     */
    void reset();

    /**
     * 转换成另外的分段流，其中偏移量和peek过的元素总数记录保持不变，原来的流会关闭
     *
     * @param interval
     * @param mapping
     * @param <T>
     * @return
     */
    <T> SegmentStream<T> map(int interval, Function<? super E, ? extends T> mapping);

    /**
     * {@link #map(int, Function)}的重载方法，其中分段间隔保持不变
     *
     * @param mapping
     * @param <T>
     * @return
     */
    default <T> SegmentStream<T> map(Function<? super E, ? extends T> mapping) {
        return map(getInterval(), mapping);
    }

    /**
     * 获取分段间隔
     *
     * @return
     */
    int getInterval();

    /**
     * 获取下一分段
     *
     * @return
     */
    List<E> nextSegment();

    /**
     * 从当前偏移量开始迭代每个元素，当 predicate 结果为false时停止
     *
     * @param predicate
     */
    default void forEach(Predicate<E> predicate) {

        Objects.requireNonNull(predicate);
        for (E e : this) {
            if (!predicate.test(e)) {
                return;
            }
        }
    }

    /**
     * 从当前偏移量开始迭代每个分段，当 predicate 结果为false时停止
     *
     * @param predicate
     */
    default void forEachSegment(Predicate<List<E>> predicate) {

        Objects.requireNonNull(predicate);
        List<E> segment;
        while (!this.isClosed() && !(segment = nextSegment()).isEmpty()) if (!predicate.test(segment)) return;
    }

    /**
     * 从当前偏移量开始迭代每个分段
     *
     * @param consumer
     */
    default void forEachSegment(Consumer<List<E>> consumer) {

        Objects.requireNonNull(consumer);
        forEachSegment(es -> {
            consumer.accept(es);
            return true;
        });
    }
}
